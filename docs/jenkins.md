# How to use our Jenkins server

## The images

After you've been connected the first thing you may want to do is to pull the images to make sure their up to date in the registry.  
Access the "Whanos base images" folder, then click on the "Build all base images" job, and click on "Build now".

It will take a few minutes to build all the images, but after that you can start using them !

## The projects

The projects are the main part of our Jenkins. First, you need to link a existing repository to a project.  
Click on the "link-project" job, and fill the form with the repository URL and the project name.

If you don't know what to put in the `ID_CREDENTIALS` field, see [this page](credentials.md).
