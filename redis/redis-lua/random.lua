for i=1, ARGV[1],1 do
 redis.call("lpush", KEYS[1], math.random()); 
end
return true
