# Hello World

###This folder is about practicing coding with leetcode examples and algorithms.

#### 1. problem: fatal: unable to access 'https://github.com/yeyechen/hello_world_java/': Failed to connect to github.com port 443 after 21106 ms: Timed out
#### solution: 
#### git config --global http.proxy http://127.0.0.1:1080
#### git config --global https.proxy https://127.0.0.1:1080
#### git config --global --unset http.proxy
#### git config --global --unset https.proxy
#### or:
#### git config --global http.proxy http://proxyuser:proxypwd@proxy.server.com:8080

#### 2. change git username:
#### git config --global user.name "YeyeChen"