# TODO list

## Essential

 - The game
  - Movement actions
  - Check for level ending
  - Ice floors
 - HUD
 - Endgame screen (remember players to wait for more levels!)
 - Patch for Heroku compliance
 - Upload to Heroku, CodeGolf and Mediavida

## Planned

 - External config file
 - Show "goaled" goals in a different fashion
 - Cuteify front website
  - Javascript for key bindings
 - Print error if no referer header
 - Auto-detect tilesize
  - Maybe add a theme config file?
  - Check for consistency when loading themes?
  - Support for non-square tiles?
 - Better lein-ring support
 - Use transients instead of refs?
 - Finish the README file
 - Favicon
 - New tiles (I should probably talk with an artist...)
 
## Needed?

 - Force no-cache?
 - P3P? (as suggested in CodeGolf's comments)
 
## Optional

 - Support for random tiles
  - Maybe I shouldn't index images by filename
  - May move *tiles* out of clojoban.ui or index by parent directory
 - "Click new" message if no session is available instead of forcing new session
 - MongoDB for long-term session storage (using memory as cache for recent sessions)
 - Alternate (optional) IP session mechanism (avoiding cross-site cookies)
  - Fall-back mechanism?
  - Do not print anything until session is up? Then, two images (one blank.)
 - Hiscore list
 - Better query args parsing (for framework extensibility)
 - Bindings for Graphics and such?
  - Maybe use Seesaw or craft my own library
 - Document thoroughly in wiki
 - Button (maybe URL part) to switch themes