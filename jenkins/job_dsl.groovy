folder('Whanos base images') {
  description('Whanos base images')
}

folder('Projects') {
  description('Contain all the projects you want')
}

supported_languages = ["c", "java", "js", "python", "befunge"]
registry = "localhost:5000"

supported_languages.each { supported_languages ->
    freeStyleJob("Whanos base images/whanos-$supported_languages") {
        steps {
              shell("docker build /var/whanos/images/$supported_languages -f /var/whanos/images/$supported_languages/Dockerfile.base -t whanos-$supported_languages")
              shell("docker tag $supported_languages:latest $registry/whanos/$supported_languages:latest")
              shell("docker push $registry/whanos/$supported_languages:latest")
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
        stringParam('GITHUB_NAME', '', 'GitHub repository owner/repo_name (e.g.: "EpitechPromo2027/B-DEV-500-REN-5-2-area-jean-yanis.jeffroy")')
        stringParam('ID_CREDENTIALS', '', 'id of the ssh key used to clone the repository')
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
            conditionalSteps {
              condition {
                and {
                  fileExists('whanos.yml', BaseDir.WORKSPACE)
                }
                steps {
                  shell('kubectl apply -f whanos.yml')
                }
              }
            }
          }
        }
    ''')
    }
  }
}
