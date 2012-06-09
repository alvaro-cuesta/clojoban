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

The process is **very** straightforward.

1. Download and install [Heroku's Toolbelt](https://toolbelt.heroku.com/).
2. Login into your Heroku account using `heroku login`.
3. This will show your app's URL upon creation (auto-generated, you can change later, but you might have to update your Git's remotes.)
4. Push Clojoban Git repository to Heroku using `git push heroku master`.
5. Scale your web process as much as you like (free Heroku only accepts either one web or one worker process) using `heroku ps:scale web=1`

And that's it! If everything went good, you should be able to access your app at the URL from step 2.

You might want to check Heroku's processes:
```
$ heroku ps
=== web: `lein run $PORT`
web.1: up for 1m
```

If you encounter any problems, just search for any tutorials for Heroku deployment. You can also read Heroku's logs for your app:
```
$ heroku logs
2012-06-09T12:36:55+00:00 heroku[slugc]: Slug compilation finished
2012-06-09T12:36:56+00:00 heroku[web.1]: Starting process with command `lein run
 59888`
2012-06-09T12:37:17+00:00 app[web.1]: Launching game server on port 59888
2012-06-09T12:37:17+00:00 app[web.1]: 2012-06-09 12:37:17.636:INFO:oejs.Server:j
etty-7.6.1.v20120215#<Server org.eclipse.jetty.server.Server@3bbbbafc>
2012-06-09T12:37:17+00:00 app[web.1]: 2012-06-09 12:37:17.692:INFO:oejs.Abstract
Connector:Started SelectChannelConnector@0.0.0.0:59888
2012-06-09T12:37:17+00:00 app[web.1]:
2012-06-09T12:37:18+00:00 heroku[web.1]: State changed from starting to up
```

## License

Copyright © 2012 Álvaro Cuesta.

Distributed under the Eclipse Public License, the same as Clojure uses. See the file [COPYING](https://github.com/alvaro-cuesta/clojoban/blob/master/COPYING).