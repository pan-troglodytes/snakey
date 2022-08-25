# snakey
a snake game with extra features like portals and varied food and user customization
<p align="center">
	<img alt="snakey01" src="https://raw.githubusercontent.com/pan-troglodytes/snakey/master/screenshots/snakey01.png"> 
	<img alt="snakey04" src="https://raw.githubusercontent.com/pan-troglodytes/snakey/master/screenshots/snakey04.png">
	<img alt="snakey05" src="https://raw.githubusercontent.com/pan-troglodytes/snakey/master/screenshots/snakey05.png">
	<img alt="snakry03" src="https://raw.githubusercontent.com/pan-troglodytes/snakey/master/screenshots/snakey03.png">
	<img alt="snakey02" src="https://raw.githubusercontent.com/pan-troglodytes/snakey/master/screenshots/snakey02.png">
	<img alt="rotten01" src="https://raw.githubusercontent.com/pan-troglodytes/snakey/master/screenshots/rotten01.png">
	<img alt="rotten02" src="https://raw.githubusercontent.com/pan-troglodytes/snakey/master/screenshots/rotten02.png">
	<img alt="rotten03" src="https://raw.githubusercontent.com/pan-troglodytes/snakey/master/screenshots/rotten03.png">
	<img alt="rotten04" src="https://raw.githubusercontent.com/pan-troglodytes/snakey/master/screenshots/rotten04.png">
	<img alt="rotten05" src="https://raw.githubusercontent.com/pan-troglodytes/snakey/master/screenshots/rotten05.png">
</p>

## build
```
git clone https://github.com/pan-troglodytes/snakey
```
```
cd snakey/src
```
then ```git checkout <branch>``` into whatever branch you want. e.g. ```server```
```
javac *.java
```
```
jar cfe snakey.jar Main *
```
execute jar file with ```java -jar snakey.jar```

## for the server branch
### server
```java -jar snakey.jar --server <port> <width of map> <height>```
### client
```java -jar snakey.jar --client <name> <ip> <port> # ip and port of server```
