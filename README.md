# Clojoban!

A little Sokoban clone for [Create a User-Profile Mini-Game](http://codegolf.stackexchange.com/questions/5933/create-a-user-profile-mini-game) puzzle at [Programming Puzzles & Code Golf StackExchange](http://codegolf.stackexchange.com) Make sure you **read the puzzle description**, this isn't just another Sokoban clone!

**In short:** this game allows you to embed it *ANYWHERE* you can embed images and links.

[![Clojoban!](http://i.imgur.com/zIuPq.png)](http://clojoban.herokuapp.com)

This project began as a fun way to make a bigger project for [Clojure](http://www.clojure.org), so I'm open to criticism (and willing to learn from it!) I aimed for modularity, good extensibility and tried to follow good practices as much as possible.

You should think of Clojoban as a framework for "remote embedabble" games, feel free to create your own games from it! (see the [license](https://github.com/alvaro-cuesta/clojoban#license) below.)

## Try it!

Unfortunately, I can't embed *Clojoban!* in this README since GitHub caches images, so you'll have to go to my [Clojoban live example at Heroku](http://clojoban.herokuapp.com) (which, to be honest, defeats the purpose of "remote embedding".)

You can see a working embedded copy at [my Programming & Code Golf user profile](http://codegolf.stackexchange.com/users/4685/lvaro-cuesta). Also, there's an embedded copy at [Create a User-Profile Mini-Game](http://codegolf.stackexchange.com/questions/5933/create-a-user-profile-mini-game) (although there are some known [limitations](https://github.com/alvaro-cuesta/clojoban#limitations).)

## Contributing

If you know Clojure, just [fork Clojoban!](https://github.com/alvaro-cuesta/clojoban/fork) and let me know if you pulled something off. Remember checking the [license](https://github.com/alvaro-cuesta/clojoban#license).

You can also contribute levels. See the [example levels](https://github.com/alvaro-cuesta/clojoban/tree/master/resources/levels) to get the feel about the format. There's an `:author` tag you might want to use (authorship will be shown in-game.)

Of course, anyone contributing will be credited accordingly in resource tags and in the [CONTRIBUTORS](https://github.com/alvaro-cuesta/clojoban/blob/master/CONTRIBUTORS.md) file

## Limitations

#### Anchors

Since you can embed the game anywhere, you'll most likely be using anchors (like `#this`) to point to the game after each click.

Unfortunately, when anchors are set, clicking on the same link twice won't work. Since the URL is the same (when clicking the same link/action twice) your browser will think you're trying to go to the anchor. It will just stay in the page and move the view (therefore, not reacting.)

To solve this, you can either add a `?noop` action link (to be pressed between repeated actions), point users to the game homepage (like [this one for Clojoban](http://clojoban.herokuapp.com)) or advice them to press `F5`.

You can experience this *issue* in my [Create a User-Profile Mini-Game](http://codegolf.stackexchange.com/questions/5933/create-a-user-profile-mini-game) puzzle post (and see the `?noop` link in action.)

#### Cross-site cookies

The "remoteness" of the game is supported by cross-site cookies. This means your browser must accept cookies from content embedded in a different domain than the current webpage.

Some browsers disable this feature (and they do it for a very good reason: privacy) so remote embedding might not work properly in some browsers/configuration cases.

*(NOTE: This issue might be resolved anytime soon!)*

#### Referer

Since Clojoban parses the `REFERER` header from HTTP requests to determine the selected action, it has to be enabled in the users' browser.

Some people disable this feature for privacy reasons, so remotely-embedded Clojoban won't work for them.

## Usage

```
- Leiningen:
lein run <port> <threads> <levels-dir theme-dir>

- lein-ring:
lein ring server-headless <port>
```

You can leave out parameters, but it must be in order (e.g., you can't specify threads without a port.) These are the default parameter values:

```
port = 1337
threads = 50
levels-dir = resources/levels
theme-dir = resources/images
```
    
## Deploying to Heroku

The process is **very** straightforward.

1. Download and install [Heroku's Toolbelt](https://toolbelt.heroku.com/).
2. Login into your Heroku account using `heroku login`.
3. Create your app using `heroku create --stack cedar`. This will show your app's URL upon creation (auto-generated, you can change later, but you might have to update your Git's remotes.)
4. Push Clojoban Git repository to Heroku using `git push heroku master` under your Git clone.
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
2012-06-09T12:36:56+00:00 heroku[web.1]: Starting process with command `lein run 59888`
2012-06-09T12:37:17+00:00 app[web.1]: Launching game server on port 59888
2012-06-09T12:37:17+00:00 app[web.1]: 2012-06-09 12:37:17.636:INFO:oejs.Server:jetty-7.6.1.v20120215 #<Server org.eclipse.jetty.server.Server@3bbbbafc>
2012-06-09T12:37:17+00:00 app[web.1]: 2012-06-09 12:37:17.692:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:59888
2012-06-09T12:37:17+00:00 app[web.1]:
2012-06-09T12:37:18+00:00 heroku[web.1]: State changed from starting to up
```

## License

Copyright © 2012 Álvaro Cuesta.

Distributed under the Eclipse Public License, the same as Clojure uses. See the file [COPYING](https://github.com/alvaro-cuesta/clojoban/blob/master/COPYING).