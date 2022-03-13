# Java + Maven + GitHub Actions = Native Desktop Apps

[JavaFX](https://openjfx.io) or Swing + [jpackage](https://docs.oracle.com/en/java/javase/15/docs/specs/man/jpackage.html) +
[Maven](http://maven.apache.org) template project for generating native desktop applications.

# Goal

1. Build nice, small cross-platform JavaFX or Swing desktop apps with native installers
    - Apx 30-40mb .dmg, .msi and .deb installers - check out the example builds in
      [releases](https://github.com/wiverson/maven-jpackage-template/releases).
    - Note that the macOS builds are not signed, and therefore macOS will report as
      damaged/unopenable. There 
      are [a few ways to deal with this](https://github.com/wiverson/maven-jpackage-template/issues/49), 
      depending on your situation.
2. Just use Maven - no shell scripts required.
	- Use standard Maven dependency system to manage dependencies
3. Generate [macOS (.dmg), Windows (.msi) and Unix (e.g. deb/rpm)](https://github.com/wiverson/maven-jpackage-template/releases)
installers/packages automatically
with [GitHub Actions](https://github.com/wiverson/maven-jpackage-template/tree/main/.github/workflows).

## Overview

This template uses a [Maven plugin](https://github.com/wiverson/jtoolprovider-plugin) to generate a custom JVM and
installer package for a JavaFX application. It can easily be adapted to work with Swing instead.

### Requirements

- [Java 17](https://adoptium.net/?variant=openjdk17) and [Maven](https://maven.apache.org/).
- On macOS XCode is required.
- On Windows the free [WiX Toolset](https://wixtoolset.org/) is required.

The project includes [GitHub Actions](https://github.com/wiverson/maven-jpackage-template/tree/main/.github/workflows)
which automatically generate macOS, Windows, and Linux installers.

The generated installers come in at around 30-40mb. The example source in the project includes demonstrations of several
native desktop features - for example, drag-and-drop from the Finder/Explorer, as well as a few macOS Dock integration
examples. Removing the code and the demonstration dependencies gets a "Hello World" build size closer to 30mb than
40mb.

## Key Features

Here are few cool things in this template:

- Only uses Java and Maven. No shell scripts required.
- Includes sample [GitHub Actions](https://github.com/wiverson/maven-jpackage-template/tree/main/.github/workflows) to
  build macOS, Windows and Linux installers
- Demonstrates setting the application icon
- Builds a .dmg on macOS, .msi on Windows, and .deb on Linux
- Bundles the JavaFX SDK & modules to simplify getting started.
	- Just delete the JavaFX stuff if you are using Swing 
- Template includes several examples of JavaFX / native desktop integration
	- Drag & drop with Finder / Explorer
	- Change the Dock icon dynamically on macOS
	- Menu on the top for macOS, in the window itself on Windows
	- Request user attention (bouncing dock icon) on macOS
- Java + Java modules are used to build a trimmed JVM ([a few thoughts on Java modules](https://changenode.com/articles/fomo-java-modules))
- The user application uses ordinary Maven dependencies and classpath to run the application
	- Nice illustration of how to use jlink to build a slim JVM, point jpackage at that JVM and still use the ordinary classpath for the application 

Once you get started, you might find these lists of tutorials, tools, libraries for
[JavaFX](https://gist.github.com/wiverson/6c7f49819016cece906f0e8cea195ea2)
and general [Java desktop integration](https://gist.github.com/wiverson/e9dfd73ca9a9a222b2d0a3d68ae3f129) helpful.

### Does this work with Apple Silicon aka M1?

Yes, although as of this writing I don't believe there are GitHub Action runners that support M1. But building locally
on my M1 laptop works great and generates native M1 builds.

### I downloaded the macOS release and it won't open - the Finder says it's damaged!

Yup, it's quarantined because it's not signed. You can override the quarantine via
a command-line option. See the next Q on macOS signing, notarization, and stapling.

### Does this support macOS signing, notarization, and stapling?

Yes, there is a GitHub Action and a Maven profile to assist with setting all of this up
for macOS applications.

For more information, see the [documentation on getting macOS signing/notarization/stapling](/docs/apple-sign-notarize.md) set 
up. It won't work out of the box, as you need to sign up for an Apple Developer account,
add a bunch of GitHub Secrets and update the pom.xml.

### What about Linux?

The JavaFX builds include several other architectures, including aarch64 and arm32. In theory,
you should be able to add those just like the other builds. Haven't tested it though.
Feel free to post in the [discussion](https://github.com/wiverson/maven-jpackage-template/discussions) section if you 
are using Linux.

### Can I Use this with Swing instead of JavaFX?

tl;dr absolutely.

It's actually a lot easier to use Swing instead of JavaFX, as Swing is built in to the JDK and doesn't require fiddling with
Java modules.

Just delete the JavaFX stuff, including the JavaFX modules and add a Swing main class instead. If you are reasonably familiar 
with Maven this shouldn't be very hard to do.

I *highly* recommend the [FlatLaf](https://www.formdev.com/flatlaf/) as a must use with Swing. That look-and-feel plus
designers such as the [IntelliJ GUI Designer](https://www.jetbrains.com/help/idea/gui-designer-basics.html) 
or [JFormDesigner](https://www.formdev.com/jformdesigner/) can work very well, arguably with an easier learning curve
than JavaFX.

In particular, delete the following directories:

```
/javafx-linux-x64_64
/javafx-osx-x86_64
/javafx-os-aarch_64
/javafx-windows-x86_64
```

Changes to the pom.xml:

1. Remove the javafx modules from the jvm.modules property
2. Remove the javafx.version property.
3. Remove the three org.openjfx dependencies
4. Remove the configuration/excludeGroupIds section from the maven-dependency-plugin
5. Remove javafx-maven-plugin from the plugins list
6. Remove the modulePath delcaration from the jtoolprovider-plugin execution/configuration

# Usage

Once everything is installed (see below) it's really easy to use:

To generate an installer, just run...

`mvn clean install`

To do everything up until the actual installer generation (including generating the custom JVM)...

`mvn clean package`

# Installation

1. Install [OpenJDK Java 17](https://adoptium.net/?variant=openjdk17) or
   [Oracle Java 17](https://www.oracle.com/java/technologies/javase-downloads.html).
	- Verify by opening a fresh Terminal/Command Prompt and typing `java --version`.
2. Install [Apache Maven 3.6.3](http://maven.apache.org/install.html) or later and make sure it's on your path.
	- Verify this by opening a fresh Terminal/Command Prompt and typing `mvn --version`.
3. macOS: verify XCode is installed and needed agreements accepted.
	- Launch XCode and accept the license, or verify in Terminal with the command `sudo xcodebuild -license`.
5. Windows: install [Wix 3 binaries](https://github.com/wixtoolset/wix3/releases/).
	- Installing Wix via the installer should be sufficient for jpackage to find it.
3. Clone/download this project.
6. Final step: run `mvn clean install` from the root of the project to generate the `target\TestApp.dmg`
   or `target\TestApp.msi` (installer).
	- Note that the actual generated installer will include a version number in the file name
	- For reference, here is a complete run log for [a successful run](docs/sample-run.md).

Because these builds use stripped down JVM images, the
[generated installers are in the 30-40mb range](https://github.com/wiverson/maven-jpackage-template/releases).

On macOS you should [add signing to avoid error messages](https://github.com/wiverson/maven-jpackage-template/issues/49)
related to the security system(s).

# Debugging

1. If the built app fails to run, make sure the JavaFX app runs as expected first by using the `mvn javafx:run` command.
   This will run the app in development mode locally, and you should see standard System.out debug lines appear in your
   console.
	- Many flavors of Linux fail to run here for a variety of reasons. Head over to
	  the [discussions](https://github.com/wiverson/maven-jpackage-template/discussions) or perhaps consider your
	  [consulting budget](https://changenode.com) or
	  a [JavaFX support contract from Gluon](https://gluonhq.com/services/javafx-support/).
2. Check the Maven build logs (of course).
3. By default, the app will generate debug*****.log files containing the output from System.out. You can look at the
   main method of `BaseApplication.java` to see how this is done. For a production app, you would want to place these
   logs in the correct OS specific location. On a Unix machine you can `tail -f` the log normally.

# OS-Specific Notes

## Linux

There are a LOT of different flavors of Linux out there. I've provided the Ubuntu build more as an example of how the
GitHub Action works, but I can't diagnose or trouble-shoot your Linux build (unless it's a consulting engagement). Feel
free to post these in [discussions](https://github.com/wiverson/maven-jpackage-template/discussions)!

I will note, however, that much of the Linux trouble I have seen comes from some of the included integration
demonstrations. Try commenting out the loading of demo plugins in `BaseApplication.java` - specifically the loop that
loads the plugins.

In theory, the Exception handler in the plugin loader code should catch the exceptions. In practice, on a few flavors of
Linux something dies with a native exception that takes it all down.

I get more support/issues for Linux builds than anything else, often for distros I've never heard of... which is cool
but not something I'm really set up to deal with (short of paid consulting). That said, every Linux support issue so
far has been resolved pretty easily by folks posting Maven or application log files in
the [discussion group](https://github.com/wiverson/maven-jpackage-template/discussions). No promises, but go forth and post!

# Help

Problems? Make sure everything is installed and working right!

- Compiler not recognizing the --release option? Probably on an old JDK.
- Can't find jdeps? Probably on an old JDK.
- Can't find jpackage on Java 15? Probably haven't set up your system
  to [allow Java 15 to enable preview packages]((docs/java-15-jpackage.md)).
- Unrecognized option: --add-modules jdk.incubator.jpackage
	- Could be a left-over MAVEN_OPTS setting when you switched from Java 15 to Java 16/17
	- If you are still on Java 15, you may not have
	  [MAVEN_OPTS set correctly](https://github.com/wiverson/maven-jpackage-template/issues/2).

If you need consulting support, feel free to reach out at [ChangeNode.com](https://changenode.com/).

# Q&A

If you are using the template, browsing the [Q&A](docs/qna.md) is highly recommended.
