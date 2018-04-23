import ballerina/io;

// Shared counter variable among multiple workers.
int counter;

function main (string... args) {
    foreach i in [1..100] {
        counterUp();
    }
    io:println("final counter value - ", counter);
}
function counterUp() {
    worker w1 {
        lock {
            // Lock the shared variable and increment the counter.
            counter = counter + 1;
        }
    }
    worker w2 {
        lock {
            // Lock the shared variable and increment the counter.
            counter = counter + 1;
        }
    }
    worker w3 {
        lock {
            // Lock the shared variable and increment the counter.
            counter = counter + 1;
        }
    }
    worker w4 {
        lock {
            // Lock the shared variable and increment the counter.
            counter = counter + 1;
        }
    }
}
