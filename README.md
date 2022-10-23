# 🎉 Happy 40th Birthday to me! 🎉

For my 40th birthday party, I had an event in the style of Caltech's Ditch Day - a series of adventures and puzzles.
Think of an escape room, except not localized to one room. (My house, my parents' house, the neighborhood - I even
buried a plastic film canister in the nearby park for the players to dig up, but a squirrel got to it first!)

A few of the puzzles involved a little bit of software, which is preserved here.

## Ultron.py

In the `ultron` directory is a small Python application that presents a UI. This was the players' main point of
interaction with this puzzle set, and appeared on the main screen in my office.

## Vue Wordle

One of the puzzles gave each player their own custom Wordle to solve! I used the open source TypeScript Wordle clone at
yyx990803/vue-wordle with gratitude, and kept it in the top-level of this repository as well. I made several edits to
allow custom success messages (which were then used in the puzzle solution in Ultron.py!)

I base64 encoded the parameters for the custom word for each player as well as the success message so they weren't just
obviously present in the URL.

### Wordle running instructions - to be run from a Windows machine in my office

* [Install nvm-windows](https://github.com/coreybutler/nvm-windows)
* Open a command line as the administrator, and `nvm install 18.6.0` then `nvm use 18.6.0`
* Run `npm install`
* Run `npm run dev`

I created a few Windows batch scripts to make it easy to launch this. I also created a small Windows batch script that
polled for the existence of a file on the I:\ drive to detect when a USB drive had been plugged in, and then launched
Ultron.py!

## richochet-robots

Another of the puzzles was based on the board game "Ricochet Robots" - a puzzle where you can move robots around on a
board until they bump into a wall or each other. To create fun and fair puzzles, there were several constraints: I
wanted each puzzle to have a _unique_ shortest solution that took 4 or 5 moves. So, I wrote this utility in Kotlin to
generate random puzzles, to _solve_ puzzles, and to find ones that matched my desired constraints. (This program wasn't
used during the event itself, just to generate puzzles printed out and then solved by hand!)