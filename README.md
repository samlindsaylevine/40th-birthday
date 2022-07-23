# Running instructions - to be run from a Windows machine in my office
* [Install nvm-windows](https://github.com/coreybutler/nvm-windows)
* Open a command line as the administrator, and `nvm install 18.6.0` then `nvm use 18.6.0`
* Run `npm run dev`

# Vue Wordle

Taken from yyx990803/vue-wordle with gratitude and used for my private party.

[Live demo](https://vue-wordle.netlify.app/)

A Vue implementation of the [Wordle game](https://www.powerlanguage.co.uk/wordle/). This is just for fun and doesn't aim to 100% replicate the original.

You can make your own Wordle and send it to friends by base64-encoding a word and include it as the URL query, e.g. https://vue-wordle.netlify.app/?YmxpbXA= (this will also allow words that are not in the dictionary.)

This repository is open sourced for learning purposes only - the original creator(s) of Wordle own all applicable rights to the game itself.
