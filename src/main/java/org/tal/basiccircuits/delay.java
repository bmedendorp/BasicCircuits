/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tal.basiccircuits;

import org.bukkit.entity.Player;
import org.tal.redstonechips.circuit.Circuit;
import org.tal.redstonechips.util.UnitParser;

/**
 *
 * @author Tal Eisenberg
 */
public class delay extends Circuit {
    private long interval;
    private long intervalInTicks;

    @Override
    public void inputChange(final int inIdx, final boolean state) {
        if (intervalInTicks==0)
            sendOutput(inIdx, state);
        else redstoneChips.getServer().getScheduler().scheduleSyncDelayedTask(redstoneChips, new Runnable() {
            @Override
            public void run() {
                sendOutput(inIdx, state);
            }
        }, intervalInTicks);
    }

    @Override
    protected boolean init(Player player, String[] args) {
        if (inputs.length!=outputs.length) {
            error(player, "Expecting the same number of inputs and outputs.");
            return false;
        }

        if (inputs.length==0) {
            error(player, "Expecting at least one input and one output.");
        }

        if (args.length==0) interval = 1000; // 1 sec default

        if (args.length>=1) {
            try {
                interval = Math.round(UnitParser.parse(args[0]));
            } catch (Exception e) {
                error(player, "Bad pulse duration argument: " + args[0]);
                return false;
            }
        }

        intervalInTicks = Math.round(interval/50);

        return true;
    }
}