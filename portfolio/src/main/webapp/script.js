// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random personal fact to the page.
 */
function addRandomFact() {
    const facts = [
        'I play the piano.',
        'Going into my freshman year of college, I had enough credits to be a Junior.',
        'My fastest solve time for the 3x3x3 Rubik\'s cube was ~15 seconds.',
        'I used to play the bagpipes. Our band won the Juvenile World Championships in 1998.',
        'I work at Google.',
        'I used to be an Android app developer.',
        'My favorite board game is Mage Knight.'
    ];

    // Pick a random fact.
    const fact = facts[Math.floor(Math.random() * facts.length)];

    // Add it to the page.
    const factContainer = document.getElementById('fact-container');
    factContainer.innerText = fact;
}

function retrieveInternLdaps() {
    fetch('/data').then(response => response.text()).then(ldaps => {
        document.getElementById('interns-container').innerText = ldaps;
    });
}
