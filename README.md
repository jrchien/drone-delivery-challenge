### Assumptions

* **ID Format (WM####):** The *Example Input File Rows* had the format as WM###, which contradicted the *Input Description*. Went with the *Input Description* format.
* **The drone has to return to the warehouse at each step:** The *Example Output File Rows* had departure times that could not be possible if the return trip took the same amount of time as the delivery trip. (ex: WM002 departs at 6AM with a distance of 5min. WM001 departs at 6:07AM when the drone wouldn't be back until at least 6:10AM)
* **The drone down time is negligible:** It isn't explicitly said in the prompt, but the assumption is that the time it takes to land/take-off and load/unload packages isn't accounted for.
* **Once the order has reached the detractor zone, it no longer matters when it is delivered.**
* **NPS is rounded to the nearest whole percentage:** This is based on the *Example Output File Rows* having an NPS of 87.
* **Since there is no concept of days, all orders that do not fit in between 6AM and 10PM are included at the end with a departure time of 23:59:59 and count as detractors.**

### Executing Tests
```
./gradlew test
```

### Build Instructions
```
./gradlew build
```