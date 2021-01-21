# Roxas - Java Discord Bot

This is a discord bot made using Discord4J and mongodb. At some point this will become obsolete but I will try to update this as I further development.
```
Dependencies: Discord4J, MongoDB
IDE: IntelliJ
```

### Current commands: 
* !ping - get network latency to discord bot gateway
* !start - create a character

## ToDo
* fix databse insertion for existing user
* load config from environment vars instead of args (token, host, port etc)

### Additional Notes
``` 
// Determine which Reactor Core operations to use
.next() = Flux -> Mono 
.map() = If returns publisher
.flatmap() = if returns 
.doOnNext() = does not return (no changes made calling runnable)


// mongodb cli
use roxas         - switch to appropriate db
show collections  - list all collections for current db
db.players.find() - query all documents in player collection
```

