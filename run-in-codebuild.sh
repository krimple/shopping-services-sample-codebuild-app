/bin/bash /Users/kenrimple/bin/codebuild_build.sh \
  -i aws/codebuild/amazon-linux-2-v-3:latest \
  -a /tmp/cb \
  -s `pwd` \
  -c \
  -m
