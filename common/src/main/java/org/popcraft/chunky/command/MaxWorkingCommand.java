package org.popcraft.chunky.command;

import org.popcraft.chunky.Chunky;
import org.popcraft.chunky.platform.Sender;
import org.popcraft.chunky.util.Input;

import java.util.Optional;

public class MaxWorkingCommand extends ChunkyCommand {
    public MaxWorkingCommand(Chunky chunky) {
        super(chunky);
    }

    public void execute(Sender sender, String[] args) {
        Optional<Integer> newMaxWorking = Optional.empty();
        if (args.length > 1) {
            newMaxWorking = Input.tryInteger(args[1]);
        }
        if (!newMaxWorking.isPresent()) {
            sender.sendMessage("help_maxWorking");
            return;
        }
        int quietInterval = Math.max(0, newMaxWorking.get());
        chunky.getOptions().setMaxWorking(quietInterval);
        sender.sendMessagePrefixed("format_maxWorking", quietInterval);
    }
}
