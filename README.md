<h1 style="font-weight:normal">
  <a href="https://sourcerer.io">
    <img src=https://user-images.githubusercontent.com/20287615/34189346-d426d4c2-e4ef-11e7-9da4-cc76a1ed111d.png alt="Sourcerer" width=35>
  </a>
  &nbsp;sourcerer.io&nbsp;
  <a href="https://sourcerer.io/start">
    <img src=https://img.shields.io/badge/sourcerer-start%20now-brightgreen.svg>
  </a>
</h1>
A visual profile for software engineers.
<br>

![profile-anishot](https://user-images.githubusercontent.com/20287615/41503360-cd85b92a-7186-11e8-94a9-c733d93e9f19.gif)
<br>

Get started
===========

Easiest way to create your profile from you public repos sourcerer.io/start

Showcase
========
<img width="255" alt="anatoly" src="https://user-images.githubusercontent.com/29913247/41805813-34dd2e1e-76b9-11e8-8879-ae01d7c1332a.png">
<img width="251" alt="adnanrahic" src="https://user-images.githubusercontent.com/29913247/41805810-34847e36-76b9-11e8-960c-87a8981f9c9c.png">
<img width="252" alt="wanghuaili" src="https://user-images.githubusercontent.com/29913247/41805811-34a17414-76b9-11e8-98b6-bd6d2f29d4ed.png">
<img width="249" alt="ddeveloperr" src="https://user-images.githubusercontent.com/29913247/41805812-34c084b2-76b9-11e8-8d5a-70a8de2044b3.png">

What is it?
===========

Once you feed [Sourcerer](https://sourcerer.io/) some git repos, you will get a beautiful profile that will help you learn things about yourself, connect to others, and become a better
engineer. 

Example profiles:<br> 
<https://sourcerer.io/sergey><br>
<https://sourcerer.io/ddeveloperr><br>
<https://sourcerer.io/wanghuaili><br>
<https://sourcerer.io/adnanrahic><br>

Both open source and closed source repos are fine. The easiest way to get started is with your open source repos. Go to <https://sourcerer.io/start>, and select *Build with GitHub* and watch your profile build. For closed source repos, you will need to use this app. If you already created an account using GitHub, you would have received an email with credentials for the app.  If not, You will need a new account, which you can get at <https://sourcerer.io/start>, and select *Build with app*.

The Sourcerer app does **NOT** upload source code anywhere, and it **NEVER** will. The app looks at repos locally on your machine, and then sends stats to sourcerer.io. The best way to verify is to look at the code. [src/main/proto/sourcerer.proto](https://github.com/sourcerer-io/sourcerer-app/blob/develop/src/main/proto/sourcerer.proto)
is a good start as it describes the client-server protocol.

Creating your profile is just the first step for us at Sourcerer. Some of the things on our roadmap include:
* News that is relevant to your code
* Engineers to follow and learn from
* Technology and libraries you should know about
* Projects that could use your help


Requirements
============

* Linux or macOS
* Java 8+ Platform ([JRE](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) for Linux or [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) for macOS)
* Git repositories with master branch with at least one commit
* Account on <https://sourcerer.io/>

Install/uninstall
=================

To install sourcerer run the following command:

```
curl -s https://sourcerer.io/app/install | bash
```

To remove sourcerer from your machine:

```
sourcerer --uninstall
```

Build
=====

To build and run this application, you'll need latest versions of Git, Gradle and JDK installed on your computer. From your command line:

```
# Clone this repository
$ git clone https://github.com/sourcerer-io/sourcerer-app.git

# Go into the repository
$ cd sourcerer-app

# Build
$ gradle build

# Run the app
$ java -jar build/libs/sourcerer-app.jar
```

Contributing
============

We love contributions!  Check out the [Contribution guide](https://github.com/sourcerer-io/sourcerer-app/blob/master/CONTRIBUTING.md) for more information.

Some handy links:<br>
* [Sourcerer Site](https://sourcerer.io/)
* [Sourcerer Blog](https://blog.sourcerer.io)
* [Follow Sourcerer on Twitter](https://twitter.com/sourcerer_io)
* [Follow Sourcerer on Facebook](https://www.facebook.com/sourcerer.io/)

License
=======
Sourcerer is under the MIT license. See the [LICENSE](https://github.com/sourcerer-io/sourcerer-app/blob/develop/LICENSE.md) for more information.
