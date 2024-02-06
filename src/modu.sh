images=( $1 )
for i in "${images[@]}" ; do 
	magick $i -modulate 100,100,160 $i
	echo $i
done
