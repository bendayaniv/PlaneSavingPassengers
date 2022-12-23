# PlaneSavingPassengers

## A little review:

All your passengers made an emergency jump from the plane following a bird attack!
Your mission - to collect as many passengers as possible on the way, and avoid collisions with the
birds! Their lives depend on you!

## How to play:

You need to move the plane right and left - by buttons or move your phone, collect as many
passengers as you can and earn points. The game is over after the third collision with a bird.

## On the project:

We needed to create a game with 3 pages - menu, game and end game.

In the menu page, you can choose the difficulty level of the game and how to move the plane. The
higher the level, the more difficult the game (there are two levels - slow and fast). You can move
the plane with buttons or by moving the phone. You can move to the game page or to the end game
page.

In the game page you get to play the game.

After you get disqualified, you go to the end game page. There you can see your scores in a list and
the best score on a map. By clicking on an object in the list you can zoom in on the location where
this particular score was achieved. Also you have the opportunity to save your score to the list and
to add another marker on the map (that depend if you give permission to use your location and you
GPS is enable). If you go to the score page from the menu, you can add nothing to the list.

We save the scores with Shared Preferences, and get them from there as well.

We request permission to access the location, and if the user allows it, we can let the player save
his score with his current location where he achieved it.

We used Google Maps API to display the scores on the map.

## Development environment:

We used Android Studio to develop the project.

https://user-images.githubusercontent.com/52703125/209184958-7a43b142-cd96-4bb2-b236-5731c253709b.mp4

