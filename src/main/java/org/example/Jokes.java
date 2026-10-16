package org.example;

public class Jokes {
    private int counter = 0;
    String[] jokes = new String[]{
            "There are only 10 people on this earth. Those who understand binary and those who don't",
            "What do you call 8 Hobbits? \n A Hobbyte",
            "Why do computer scientists always confuse halloween and christmas? \n Because 31 Oct = 25 Dec",
            "God is real. Unless defined as an integer",
            "Programmers wife asks him to go to the store. She tells him: Buy bread and if they have eggs, get a dozen. \n He comes home with 12 loaves of bread.",
            "What’s the difference between a delicious dinner and a slow computer? \n One’s a rack of lamb and the other’s a lack of RAM.",
            "You know what screams “I’m insecure”? \n http://",
            "Why do software developers use dark themes?\n Because bugs are attracted to the light.",
            "My team had a debate on what the best looping variable name is. \n i won"
    };

    public String getJoke() {
        return this.jokes[counter++ % this.jokes.length];

    }
}
