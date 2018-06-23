<h1 style="font-weight:normal">
  <a href="https://sourcerer.io">
    <img src=https://user-images.githubusercontent.com/20287615/34189346-d426d4c2-e4ef-11e7-9da4-cc76a1ed111d.png alt="Sourcerer" width=35>
  </a>
  &nbsp;sourcerer.io&nbsp;
  <a href="https://sourcerer.io/start"><img src=https://img.shields.io/badge/sourcerer-start%20now-brightgreen.svg></a>
  <a href="https://github.com/sourcerer-io/sourcerer-app/releases"><img src=https://img.shields.io/github/release/sourcerer-io/sourcerer-app.svg?colorB=4ec528></a>
  <a href="https://github.com/sourcerer-io/sourcerer-app/blob/master/LICENSE.md"><img src=https://img.shields.io/github/license/sourcerer-io/sourcerer-app.svg></a>
</h1>

A visual profile for software engineers.
<br>

<img height="350" alt="sergey" src="https://user-images.githubusercontent.com/20287615/41503360-cd85b92a-7186-11e8-94a9-c733d93e9f19.gif">
<br>

Features
========
* Build profile automatically with a single click
* Support of 100 languages (even exotic like COBOL)
* More than 1000 libraries usage detection with per-line statistics
* Visual repsentation of your developing experience
* *Finally!* Summary of all repositories you've contributed too 
* Discover interesting fun facts about yourself
* News that is relevant to your code

Creating your profile is just the first step for us at Sourcerer. Some of the things on our roadmap include:
* Engineers to follow and learn from
* Technology and libraries you should know about
* Projects that could use your help

Get started
===========

Easiest way to create your profile from you public repos sourcerer.io/start

Showcase
========

<table>
  <tr>
    <td>
      <a href="https://sourcerer.io">
        <img height="200" alt="anatoly" src="https://user-images.githubusercontent.com/29913247/41805813-34dd2e1e-76b9-11e8-8879-ae01d7c1332a.png">
      </a>
    </td>
    <td>
      <a href="https://sourcerer.io">
        <img height="200" alt="adnanrahic" src="https://user-images.githubusercontent.com/29913247/41805810-34847e36-76b9-11e8-960c-87a8981f9c9c.png">
      </a>
    </td>
    <td>
      <a href="https://sourcerer.io">
        <img height="200" alt="wanghuaili" src="https://user-images.githubusercontent.com/29913247/41805811-34a17414-76b9-11e8-98b6-bd6d2f29d4ed.png">
      </a>
    </td>
    <td>
      <a href="https://sourcerer.io">
        <img height="200" alt="ddeveloperr" src="https://user-images.githubusercontent.com/29913247/41805812-34c084b2-76b9-11e8-8d5a-70a8de2044b3.png">
      </a>
    </td>
    <td>
      <a href="https://sourcerer.io">
        <img height="200" alt="ddeveloperr" src="https://user-images.githubusercontent.com/29913247/41805812-34c084b2-76b9-11e8-8d5a-70a8de2044b3.png">
      </a>
    </td>
    <td>
      <a href="https://sourcerer.io">
        <img height="200" alt="anatoly" src="https://user-images.githubusercontent.com/29913247/41805813-34dd2e1e-76b9-11e8-8879-ae01d7c1332a.png">
      </a>
    </td>
    <td>
      <a href="https://sourcerer.io">
        <img height="200" alt="wanghuaili" src="https://user-images.githubusercontent.com/29913247/41805811-34a17414-76b9-11e8-98b6-bd6d2f29d4ed.png">
      </a>
    </td>
    <td>
      <a href="https://sourcerer.io">
        <img height="200" alt="adnanrahic" src="https://user-images.githubusercontent.com/29913247/41805810-34847e36-76b9-11e8-960c-87a8981f9c9c.png">
      </a>
    </td>
  </tr>
</table>

What is it?
===========

Once you feed [Sourcerer](https://sourcerer.io/) some git repos, you will get a beautiful profile that will help you learn things about yourself, connect to others, and become a better
engineer. 

Both open source and closed source repos are fine. The easiest way to get started is with your open source repos. Go to <https://sourcerer.io/start>, and select *Build with GitHub* and watch your profile build. For closed source repos, you will need to use this app. If you already created an account using GitHub, you would have received an email with credentials for the app.  If not, You will need a new account, which you can get at <https://sourcerer.io/start>, and select *Build with app*.

The Sourcerer app does **NOT** upload source code anywhere, and it **NEVER** will. The app looks at repos locally on your machine, and then sends stats to sourcerer.io. The best way to verify is to look at the code. [src/main/proto/sourcerer.proto](https://github.com/sourcerer-io/sourcerer-app/blob/develop/src/main/proto/sourcerer.proto)
is a good start as it describes the client-server protocol.


Requirements
============

* Linux or macOS
* Java 8+ Platform ([JRE](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) for Linux or [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) for macOS)
* Git repositories with master branch with at least one commit
* Account on <https://sourcerer.io/>

Usage
=====

To install sourcerer run the following command:

```
curl -s https://sourcerer.io/app/install | bash
```

To run wizard use "sourcerer" command

Uninstall?
==========

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

FAQ
===
How to process close source repos?
We process only public repos using GitHub OAuth. To process close source repos you need to run sourcerer app locally. See get started for instructions. Sourcerer app sends only statistical information to our servers and never sends code.

Why do you need THIS permissions?
We use emails to indetify commits authorship, read orgs access to get list of public repositories that you've contributed to. You also need to grant access to read this public information from org

Other questions
See sourcerer.io/faq 

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
