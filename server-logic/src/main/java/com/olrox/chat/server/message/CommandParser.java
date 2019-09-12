package com.olrox.chat.server.message;

public class CommandParser {
    public static CommandType parse(String commandString) {
        if(commandString == null) {
            return CommandType.EXIT;
        }

        if(!commandString.startsWith("/")) {
            return CommandType.MESSAGE;
        }

        for(CommandType command : CommandType.values()){
            if(commandString.startsWith(command.getCode())) {
                return command;
            }
        }

        return CommandType.MESSAGE;
    }
}
