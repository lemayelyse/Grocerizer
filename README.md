# Grocerizer

# A simple Work In Progress app to pull weekly grocery ads and compare them to a keyword list

PLEASE NOTE: For now, currently the URL is manually hardcoded because I haven't figured out how to generate or fetch it automatically. It must be updated every week. So if the Fetch step doesn't work (you will see a message in the console), you have to go to fredmeyer.com/weeklyad and "inspect" the webpage to find the URL for the JSON source (it's in the Network tab). Or just poke me and I'll push an update.

Instructions:

Enter as many keywords as you like (one at a time) in the text field, pressing Enter for each one
This will populate your grocery list. You can print the list with Print. It will print to the Android "Run" console.
Future versions will actually show something on the app screen.
Try keywords like "steak" and "chips".

When you have finished entering your list, you can Print it, Clear it, or Fetch the grocery ad. 
Pressing Fetch will pull Json data from the weekly ad and compare that data to your keywords list.
Then it will print out to console your list containing the names and prices of matching items.
