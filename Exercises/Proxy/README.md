Exercise - Write a Proxy that implements schedule-based access

Create an interface with a few methods on it (the methods can be anything you want).
Write a class that implements your interface (the method implementations can be anything you want).
Write a proxy class that can be used to control on what days of the week and at what times of day methods can be called on instances of the class created in step 2.  Your proxy class should have the following properties:
AllowedDays: A list of days of the week on which method calls are allowed (e.g., M, T, W, Th, F)
AllowedTimeRange: A time range during which methods calls are allowed (e.g., 8 am - 5 pm)
When a method is called on the proxy, it should check the current day and time.  If the method call is allowed, it should be passed to the “real object’ and the result returned to the caller.  If the method call is not allowed, the proxy should throw an exception at the caller.
TIP: You can use Java's GregorianCalendar class (all Java versions) or LocalDateTime class (preferred for Java version 8 or later) to get the current day of week and current hour of day.
GregorianCalendar (in java.util package)
Calendar calendar = new GregorianCalendar();
int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
LocalDateTime (in java.time package)
LocalDateTime dateTime = LocalDateTime.now();
DayOfWeek currentDay = dateTime.getDayOfWeek();
int hour = dateTime.getHour();
Write a small program to test your proxy and demonstrate that it works. You need not write automated test cases. Just write a very short program you can use to test whether your proxy works.
Exercise - Write a Proxy that implements lazy loading

Create an interface named Array2D that represents a two-dimensional array of integers and contains the following methods:
A method that sets the value of an array element: set(row, col, value)
A method that returns the value of an array element: get(row, col)
Create a class that implements the Array2D interface and that also has the following methods:
Two constructors. One that lets the caller specify the dimensions of the array and one that takes a file name from which a saved array can be read.
A method that saves the object’s state to a file: save(fileName)
A method that loads the object’s state from a file: load(fileName)
TIP: Here's a Stack Overflow article that shows how to most easily serialize and deserialize a 2D array in Java.
https://stackoverflow.com/questions/1467193/java-serialization-of-multidimensional (Links to an external site.)
Write a program that creates an instance of the Array2D implementing class you created in step 2, populates it with values, and saves it to a file.  This file will be used in the next step.
Write a proxy class that implements lazy loading for your 2-D array class.
What is lazy loading?  Suppose you have an object stored in a file or database. If the object is large, it will be time-consuming to load, and you might not want to incur the expense of loading the object into RAM until the program actually uses the object. This way, if the program never uses the object, you will not waste time loading it.
An elegant way to implement this idea is with the Proxy pattern.
Create a proxy that implements the same interface as the large object
The proxy class should also have a constructor that accepts the name of the file that stores the object
When the program begins, create a proxy object that represents the large object.  Initially, the proxy will contain a null reference to the “real object”, because it hasn’t been loaded yet.
The first time a method is called on the proxy, it should load the large object from the file into RAM, and store a reference to the loaded object.
After loading the object, all method calls on the proxy should be delegated to the “real object” that was loaded.
Write a small program to test your proxy and demonstrate that it works. You need not write automated test cases. Just write a very short program you can use to test whether your proxy works.


Submission

Submit as individual .java files. DO NOT SUBMIT A ZIP FOLDER.
Exercise 1
All file names should start with "Part1"
Submit the following files
File containing the the created interface
File containing the implementation of the created interface
File containing the proxy class for scheduling
Main file containing your tests for the proxy
Exercise 2
All file names should start with "Part2"
Submit the following files
File containing the interface "Array2D"
File containing the implementation of the "Array2D" interface
File containing the lazy-loading proxy
Main file containing your tests for the proxy