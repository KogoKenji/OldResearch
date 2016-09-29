library(e1071)
library(descr)
library(cluster)

custom1 <- read.table("/Users/clear2020/Dropbox/test/custom1.txt")
custom2 <- read.table("/Users/clear2020/Dropbox/test/custom2.txt")
dim(custom1)

plot(custom1,type="l")
plot(custom2,type="l")
