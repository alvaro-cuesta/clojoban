# TODO list

## Essential

 - The game
  - Actions: up, down, left, right
  - Ice floors
 - HUD
 - Endgame screen (remember players to wait for more levels!)
 - Patch for Heroku compliance
 - Upload to Heroku, CodeGolf and Mediavida

## Planned

 - Remove **:empty** hack
 - External config file
 - Cuteify front website
 - Print error if no referer
 - Auto-detect tilesize
  - Maybe add a theme config file?
 - Better lein-ring support
 - Use transients instead of refs
 - Finish the README file
 - Favicon
 - New tiles
 
## Needed?

 - Force no-cache?
 - P3P? (as suggested in CodeGolf's comments)
 
## Optional

 - Support for random tiles
  - Maybe I shouldn't index images by filename
  - May move *tiles* out of clojoban.ui or index by parent directory
 - "Click new" message if no session is available instead of forcing new
 - MongoDB for long-term session storage (using memory as cache for recent sessions)
 - Alternate (optional) IP session mechanism (avoiding non-cross-site cookies)
  - Fall-back mechanism?
  - Do not print anything until session is up? Then, two images (one blank.)
 - Hiscore list
 - Better query args parsing (for extensibility)
 - Bindings for Graphics and such?
 - Document in wiki
 - Button (maybe URL part) to switch themes