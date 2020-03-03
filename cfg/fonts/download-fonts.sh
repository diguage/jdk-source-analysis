#!/usr/bin/env bash
#
# Download the fonts

wget https://github.com/diguage/jdk-source-analysis/releases/download/source-fonts.ttf/fonts.ttf.tgz

tar -zxvf fonts.ttf.tgz


# Backup download URL: https://github.com/adobe-fonts/source-han-serif/archive/1.001R.zip

# Maybe you have to convert the OTF fonts to TTF fonts by the otf2ttf tools of AFDKO(https://github.com/adobe-type-tools/afdko/)
