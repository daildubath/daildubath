# MentorMetrics: The 1:1 Talk-Time Dashboard

A React and TypeScript-based web application designed to help coordinators track, visualize, and analyze mentor versus mentee speaking time during 1:1 sessions. It features live pie chart visualizations, keyboard shortcuts for seamless switching, text fields for performance feedback, and the ability to export/import session data as `.txt` files.
The live website can be found here: https://daildubath.github.io/Mentor-One-on-One-Dashboard/

## Instructions for Build and Use

Steps to build and/or run the software:

1. Open the project folder in your terminal.
2. Run `npm install` to download all necessary dependencies.
3. Run `npm run dev` to start the local development server and view the app in your browser.

Instructions for using the software:

1. Press the **Spacebar** to toggle the active speaker (Mentor/Mentee) and start the timer.
2. Press **Ctrl + Space** to pause or resume the timers. (Clicking into any text box also auto-pauses the timer).
3. Fill out the feedback boxes, and use the **Export (.txt)** button to save the session data, or **Import (.txt)** to load a past session.

## Development Environment

To recreate the development environment, you need the following software and/or libraries with the specified versions:

* WebStorm IDE
* Node.js & npm
* Vite (React Framework)
* TypeScript
* Recharts (Data visualization library)

## Useful Websites to Learn More

I found these websites useful in developing this software:

* [Google Gemini](https://gemini.google.com/): Assisted heavily in generating the React and TypeScript code for this zero-code challenge.
* [W3Schools TypeScript Tutorial](https://www.w3schools.com/typescript/): Used to learn TypeScript basics and syntax.
* [TypeScript Official Docs](https://www.typescriptlang.org/docs/): Served as backup reference material for strict typing.
* *Note: The final wireframe used to design this project is included in the repository as a PNG.*

## Future Work

The following items I plan to fix, improve, and/or add to this project in the future:

* [x] Add a dedicated "Reset Session" button to clear the board without needing to refresh the page.
* [ ] Implement browser `localStorage` so session data is preserved even if the tab is accidentally closed.
* [ ] Build a more robust file parser to handle user-edited or severely malformed `.txt` files without relying purely on string matching.
* [x] Refactor the timer to calculate elapsed time using `Date.now()` instead of `setInterval` to eliminate JavaScript timer drift for hyper-accurate tracking.