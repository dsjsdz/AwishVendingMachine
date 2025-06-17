SRC_ICON=./app/src/main/res/drawable/android.png
DIR_RES=./app/src/main/res

.PHONY: icons
icons:
	magick $(SRC_ICON) -resize 162x162 ${DIR_RES}/drawable/ic_launcher_foreground.webp
	magick $(SRC_ICON) -resize 162x162 ${DIR_RES}/mipmap-hdpi/ic_launcher_foreground.webp
	magick $(SRC_ICON) -resize 162x162 ${DIR_RES}/mipmap-hdpi/ic_launcher_round.webp
	magick $(SRC_ICON) -resize 162x162 ${DIR_RES}/mipmap-hdpi/ic_launcher.webp
	magick $(SRC_ICON) -resize 162x162 ${DIR_RES}/mipmap-mdpi/ic_launcher_foreground.webp
	magick $(SRC_ICON) -resize 162x162 ${DIR_RES}/mipmap-mdpi/ic_launcher_round.webp
	magick $(SRC_ICON) -resize 162x162 ${DIR_RES}/mipmap-mdpi/ic_launcher.webp
	magick $(SRC_ICON) -resize 216x216 ${DIR_RES}/mipmap-xhdpi/ic_launcher_foreground.webp
	magick $(SRC_ICON) -resize 162x162 ${DIR_RES}/mipmap-xhdpi/ic_launcher_round.webp
	magick $(SRC_ICON) -resize 162x162 ${DIR_RES}/mipmap-xhdpi/ic_launcher.webp
	magick $(SRC_ICON) -resize 324x324 ${DIR_RES}/mipmap-xxhdpi/ic_launcher_foreground.webp
	magick $(SRC_ICON) -resize 144x144 ${DIR_RES}/mipmap-xxhdpi/ic_launcher_round.webp
	magick $(SRC_ICON) -resize 144x144 ${DIR_RES}/mipmap-xxhdpi/ic_launcher.webp
	magick $(SRC_ICON) -resize 432x432 ${DIR_RES}/mipmap-xxxhdpi/ic_launcher_foreground.webp
	magick $(SRC_ICON) -resize 192x192 ${DIR_RES}/mipmap-xxxhdpi/ic_launcher_round.webp
	magick $(SRC_ICON) -resize 144x144 ${DIR_RES}/mipmap-xxxhdpi/ic_launcher.webp
