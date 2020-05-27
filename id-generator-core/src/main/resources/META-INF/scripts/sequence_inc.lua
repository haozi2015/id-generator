local runtime_sequence_key = KEYS[1]
local inc = tonumber(ARGV[1])
local initial_value = tonumber(ARGV[2])

if redis.call("GET", runtime_sequence_key)
then
    return redis.call("INCRBY", runtime_sequence_key, inc)
else
    return redis.call("INCRBY", runtime_sequence_key, inc + initial_value)
end
