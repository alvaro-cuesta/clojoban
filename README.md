# Clojoban!

*(TODO: sample image)*

A little Sokoban clone for [Create a User-Profile Mini-Game](http://codegolf.stackexchange.com/questions/5933/create-a-user-profile-mini-game) puzzle at [Programming Puzzles & Code Golf StackExchange](http://codegolf.stackexchange.com) Make sure you **read the puzzle description**, this isn't just another Sokoban clone!

**In short:** this game allows you to embed it *ANYWHERE* you can embed images and links.

This project began as a fun way to make a bigger project for [Clojure](http://www.clojure.org), so I'm open to criticism (and willing to learn from it!) I think I did a good job in the end. I aimed for modularity, good extensibility and tried to follow good practices as much as possible. This is more like a framework for "remote embedabble" games, feel free to create your own games from it! (see the [license](https://github.com/alvaro-cuesta/clojoban#license) below.)

## Try it!

Play right here in GitHub! (*TODO*)

Or go to my Clojoban live example at Heroku. (*TODO*)

## Features

*TODO*

## Limitations

*TODO*

## Usage

You can run Clojoban in several ways:

```
- Clojure:
TODO

- Leiningen:
lein run <port> <levels-dir>

- lein-ring:
lein ring server-headless <port>
```

Default parameter values:

```
port = 1337
levels-dir = resources/levels
theme-dir = resources/images
```
    
## Deploying to Heroku

*TODO*

## License

Copyright © 2012 Álvaro Cuesta.

Distributed under the Eclipse Public License, the same as Clojure uses. See the file [COPYING](https://github.com/alvaro-cuesta/clojoban/blob/master/COPYING).