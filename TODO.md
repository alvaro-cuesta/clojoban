# TODO list

## Essential

 - The game
  - Actions: up, down, left, right
  - Ice floors
 - Endgame screen (remember players to wait for more levels!)
 - Print error if no referer
 - Patch for Heroku compliance
 - Upload to Heroku, CodeGolf and Mediavida

## Planned

 - Better lein-ring support
 - Use transients instead of refs
 - Finish the README file
 - Favicon
 - New tiles
 
## Needed?

 - Force no-cache?
 - P3P? (as suggested in CodeGolf's comments)
 
## Optional

 - "Click new" message if no session is available instead of forcing new
 - MongoDB for long-term session storage (using memory as cache for recent sessions)
 - Alternate (optional) IP session mechanism (avoiding non-cross-site cookies)
  - Fall-back mechanism?
  - Do not print anything until session is up? Then, two images (one blank.)
 - Hiscore list
 - Better query args parsing (for extensibility)
 - Bindings for Graphics and such?
 - Several tiles picked randomly