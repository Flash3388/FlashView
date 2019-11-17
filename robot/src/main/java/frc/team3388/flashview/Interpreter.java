package frc.team3388.flashview;

import com.flash3388.flashlib.robot.scheduling.ActionGroup;
import com.flash3388.flashlib.robot.scheduling.ExecutionOrder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import frc.team3388.flashview.CommandTypes.CommandType;

import java.io.BufferedReader;
import java.io.IOException;

public class Interpreter {
    private static final String TYPE_KEY = "type";
    private static final String PARAMETERS_KEY = "parameters";

    private CommandType[] commandTypes;

    public Interpreter(CommandType... commandTypes) {
        this.commandTypes = commandTypes;
    }

    public ActionGroup generateProgram(BufferedReader jsonString) throws IOException {
        ActionGroup actionGroup = new ActionGroup(ExecutionOrder.SEQUENTIAL);
        JsonArray jsonArray = toJsonArray(jsonString);
        jsonString.close();

        for(int i = 0; i < jsonArray.size(); ++i) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();

            for(CommandType commandType : commandTypes) {
                if(object.get(TYPE_KEY).getAsString().equals(commandType.getCommandType()))
                    actionGroup.add(commandType.getAction(object.get(PARAMETERS_KEY).getAsJsonObject()));
            }
        }

        return actionGroup;
    }

    private JsonArray toJsonArray(BufferedReader jsonString) {
        JsonParser parser = new JsonParser();
        return parser.parse(jsonString).getAsJsonArray();
    }
}
