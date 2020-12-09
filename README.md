# rabbitFT
[![Maven Central](
https://maven-badges.herokuapp.com/maven-central/com.github.jweavers/rabbitft/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.github.jweavers/rabbitft) 

Rabbit File Transfer is an open source java library for sharing file(s) over SFTP or Sharepoint channel. It determines possible parallel threads and initiate file transfer using available multiple threads.


## Table of contents
 * [Quick Start](https://github.com/jweavers/rabbitft#quick-start)
 * [Bugs](https://github.com/jweavers/rabbitft#bugs)
 * [Prerequisite for Sharepoint](https://github.com/jweavers/rabbitft/blob/main/sharepoint-connection-prerequisite.md)
 * [Copyright and license](https://github.com/jweavers/rabbitft#copyright-and-license)
 
 ### Quick Start
 
 To quick start, include rabbitFT to your project dependency :
 
 ```
  <dependency>
    <groupId>com.github.jweavers</groupId>
    <artifactId>rabbitft</artifactId>
    <version>0.1.0</version>
  </dependency>
 ```
 
 In your code, create `ConnectionContext` for the specific channel.
 
 **SFTP**
 
 ```
 ConnectionContext ctx = new SftpContext(host_name, port_no, user, sftp_folder_path, password);
 ```
 
  **Sharepoint**
 
 ```
 ConnectionContext ctx = new SharepointContext(client, secret, sharepoint_domain, sharepoint_folder_path, bearer);
 ```
 ***Note :** In most cases, `sharepoint_folder_path` starts with `Documents` folder. So, it's value would be `Documents\<your folder path>`.*

Now using `ConnectionContext`, you can get fresh channel instance for uploading files.

```
RabbitFT rabbitft = FileTransfer.newChannelInstance(ctx);
rabbitft.upload(files);
```

That's it ! :)

To enable debug mode, add following line before calling `upload`  :

```
rabbitft.setConsoleDebugMode(true);
```

 ### Bugs
 
* If you found any problem/issue, you can [create new issue](https://github.com/jweavers/rabbitft/issues/new).

### Copyright and license

Code and documentation Copyright (c) 2020 jweavers. Code released under the [MIT License](https://github.com/jweavers/rabbitft/blob/main/LICENSE).
