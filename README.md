# fetch-takehome

**Name:** Jian He

**Instruction (choose idle port if 8080 is not available):**

```bash
$ docker build -t fetch-takehome-jh .    
$ docker run -d --name fetch-takehome-jh -p 8080:8080 fetch-takehome-jh
```bash

Stop and clean up everything:
$ docker stop fetch-takehome-jh && docker rm fetch-takehome-jh && docker rmi fetch-takehome-jh
