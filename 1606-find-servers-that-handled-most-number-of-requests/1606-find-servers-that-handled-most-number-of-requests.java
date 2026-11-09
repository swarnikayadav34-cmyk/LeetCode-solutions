
import java.util.*;

class Solution {

    public List<Integer> busiestServers(int k, int[] arrival, int[] load) {

        // Available servers
        TreeSet<Integer> available = new TreeSet<>();

        for (int i = 0; i < k; i++) {
            available.add(i); 
        }

        // Busy servers: {finishTime, serverId}
        PriorityQueue<long[]> busy = new PriorityQueue<>(
            (a, b) -> Long.compare(a[0], b[0])
        );

        // Count requests handled by each server
        int[] count = new int[k];

        for (int i = 0; i < arrival.length; i++) {

            // Release servers that have completed their requests
            while (!busy.isEmpty() &&
                   busy.peek()[0] <= arrival[i]) {

                long[] server = busy.poll();

                available.add((int) server[1]);
            }

            // If all servers are busy, drop the request
            if (available.isEmpty()) {
                continue;
            }

            // Preferred server
            int target = i % k;

            // Find the next available server
            Integer serverId = available.ceiling(target);

            // Wrap around if necessary
            if (serverId == null) {
                serverId = available.first();
            }

            // Assign request
            available.remove(serverId);

            count[serverId]++;

            // Calculate completion time
            long finishTime = (long) arrival[i] + load[i];

            busy.offer(new long[]{finishTime, serverId});
        }

        // Find maximum requests handled
        int max = 0;

        for (int c : count) {
            max = Math.max(max, c);
        }

        // Collect busiest servers
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            if (count[i] == max) {
                result.add(i);
            }
        }

        return result;
    }
}