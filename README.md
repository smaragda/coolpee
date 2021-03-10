# Routing application

## How to build app
App can be built by running "mvn clean install" command

## running app
App can be run by executing command "mvn springboot:run"

## algorithm
I used "radar" approach. 
1. path1: source-->Border-->border of borders-->reach destination
2. path2: destination-->Border-->border of borders-->reach source
3. remember borders and narrows them by intersection of path1 and path2

May be it would be better to use graph theory on this.

