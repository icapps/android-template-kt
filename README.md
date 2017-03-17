# _OLLIE_NAME_

In this README file you can find some documentation on how we handle everything
in this project.

## Dependencies

On order to do everything you'll need to install the following dependencies:

```bash

gem install fastlane
gem install icapps-translations
```

Here is a list over version we require:

- Fastlane **2.12.0**

## Folder structure

We keep our Android folders mapped to the directories on your disk. This requires
no manual action.

## Coding Guidelines

We follow our coding guidelines that can be found on Github.


## Translations

We manage the translations with our online [translation
tool](http://translations.icapps.com). In order to import the translations you
can run the following command.

```bash
translations import
```

## Pull Request

When submitting a pull-request please take the following into account:

1. Sync the project.
2. Make sure you merged `develop` into your feature branch.
3. Run pmd findbugs and ensure there are no remaining warnings.

## Deployment

We deploy a beta application with the `beta lane`. This lane will build the
application with the _Beta_ configuration and upload it to HockeyApp. Execute
the upload like this:

```bash
fastlane beta
```

## Contributors

These are the devs resposible for this project:

- 🤓  Ollie
