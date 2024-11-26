folder('Whanos base images') {
  description('Whanos base images')
}

folder('Projects') {
  description('Contain all the projects you want')
}

supported_languages = ["c", "java", "javascript", "python", "befunge"]
registry = "{{ registry }}"

supported_languages.each { supported_languages ->
    freeStyleJob("Whanos base images/whanos-$supported_languages") {
        steps {
              shell("docker build /opt/registry/$supported_languages -f /opt/registry/$supported_languages/Dockerfile.base -t whanos-$supported_languages")
              shell("docker login -u {{ registry_user }} -p {{ registry_password }} $registry")
              shell("docker tag whanos-$supported_languages $registry/whanos-$supported_languages")
              shell("docker push $registry/whanos-$supported_languages")
        }
    }
}

freeStyleJob("Whanos base images/Build all base images") {
    publishers {
        downstream(
            supported_languages.collect { lang -> "Whanos base images/whanos-$lang" }
        )
    }
}

freeStyleJob('link-project') {
  parameters {
        stringParam('DISPLAY_NAME', '', 'Display name for the job')
        stringParam('GITHUB_NAME', '', 'GitHub repository owner/repo_name (e.g.: "git@github.com:neo-jgrec/whanos-epitech.git")')
        stringParam('ID_CREDENTIALS', '', 'id of the ssh key used to clone the repository (e.g.: "jenkins-ssh-key". You can create one in the jenkins credentials page)')
  }
  steps {
    dsl {
      text('''
        freeStyleJob('Projects/' + DISPLAY_NAME) {
          wrappers {
            preBuildCleanup()
          }
          scm {
            git {
              remote {
                name(DISPLAY_NAME)
                url(GITHUB_NAME)
                credentials(ID_CREDENTIALS)
              }
              branch('main')
            }
          }
          triggers {
            scm('* * * * *')
          }
          steps {
            shell('/var/whanos/docker-deploy.sh $DISPLAY_NAME')
          }
        }
    ''')
    }
  }
}
