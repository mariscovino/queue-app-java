# Artist Performance Queue

## Project description

<div style="text-align: justify"> 

I have been a professional singer for more than 10 years, 
and one of the things I most love about performing is interacting with my audience. To do that, 
I enjoy getting song suggestions from the public. However, this process is often not as efficient
as I would like. How it usually goes is:

</div>

1. I ask for song suggestions.
2. The audience writes the desired song on a paper.
3. The person gives it to the waiter.
4. The waiter gives the paper to me.
5. I evaluate the song suggestion.
6. Add the song to the queue.

<div style="text-align: justify"> 

Besides not being efficient, it is firstly not very ecological, given that lots of paper is "wasted"
for it to happen. Secondly, people often ask me to play songs that I am not comfortable playing, 
that are not in my repertoire, or that I already played previously. Still I cannot give this feedback 
to the person that asked for it.

With these issues and a need in mind, I decided to create an Artist Performance Queue. The program will
have different windows: audience windows, and an artist window. 

The audience window will allow the user to request songs for the artist. If the song was already played,
it will automatically be denied. If that is not the case, the songs will then automatically
be sent to the artist window. 

The artist window will allow the artist to deny or accept song requests, and list songs that were already played. 
If the song is denied, the artist
will have the option to give a feedback message. For example: "Sorry! This song is not on my repertoire, 
but I will try to learn it for next time!". If the song is accepted, the song will enter the queue, and 
the audience will receive the message "Your song request entered xxx artist's song queue".

The program will be used by artists like me and the audience present at the performance. With the program, 
song requests will be way more efficient.

## User stories

**As the audience user, I would like to:**
- Request a song.
- If my song was accepted, receive feedback if it was added to the queue.
- If my song was already played, receive feedback that it was already played.
- If my song is already in the queue, receive feedback that it is already in the queue.
- If my song was denied by the artist, receive the artist's feedback giving a reason why it was denied.
- Save the songs I already requested during that performance.
- Reset my song requests if I am at a new performance.

**As the artist user, I would like to:**
- Accept or deny song requests (add songs to the song queue or not).
- View the song queue.
- Provide feedback to the audience if I reject a song request.
- Keep track of the songs I already played.
- Save the song queue and songs I already played during that performance.
- Reset song queue if I am at a new performance.

## Phase 4: Task 3
Given the UML diagram for the final version of the code, I think the first thing that one can notice is the large 
amount of classes.
With this in mind, I think the first refactoring I would perform if I had more time to work on the project would maybe 
be to, in the UI package, unite all the artist actions in one ArtistWindow class, and unite all the audience actions in
one AudienceWindow class. For example uniting DenySong, AcceptSong etc with ArtistMain. This would make my code more 
readable and less confusing. With everything separated into different classes things can get hard to find. However, I 
would keep the abstract classes from the UI, such as QueueWindows and EnterSong, since they help avoid duplicate code.

Another refactoring I would do would be overriding equals and hashcode in the Song class. Since I wrote this code 
before knowing this was possible, I have a lot of duplicate code to find songs in a list, especially in the model 
package.

Finally, the last refactoring I would do would be to further abstract all methods that need to find something in a list.
Whether it's finding a feedback or song in a LinkedList of Feedback or Song, all methods that require this have the same
idea: iterating over a for loop until the desired Song or feedback is found. So, I would create a function that returns 
true when Object o is found in LinkedList<Object> l. Then, I would use this in the condition of any if statements in
functions that do something when a Song or Feedback was found in a list.

</div>
