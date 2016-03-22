package Logic;

import java.util.ArrayList;
import java.util.Stack;

import Storage.localStorage;
import Task.Task;

public class Undo {
	private static final String NO_PAST_COMMAND = "There are no remaining commands that can be undone";
	private static Undo undo;
	private Stack<ArrayList<Task>> completedStack, uncompletedStack;
	private ArrayList<String> pastCommands;
	
	private Undo() {
		completedStack = new Stack<ArrayList<Task>>();
		uncompletedStack = new Stack<ArrayList<Task>>();
		pastCommands = new ArrayList<String>();
	}

	public static Undo getInstance() { //singleton
		if (undo == null) {
			undo = new Undo();
		}
		return undo;
	}
	
	public String viewPastCommands() {
		String result = "";
		if (pastCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		int count = 0;
		for (String s : pastCommands) {
			count++;
			result += count + ". " + s + "\n";
		}
		return result.substring(0, result.length() - 1);
	}
	
	// adds the state of the arraylists into stacks for undo purposes. The command executed is also stored.
	public void addPreviousState(ArrayList<Task> completedOld, ArrayList<Task> uncompletedOld, String command) {
		completedStack.push(completedOld);
		uncompletedStack.push(uncompletedOld);
		pastCommands.add(command);
	}
	
	public ArrayList<Task> getLastCompletedState() {
		return completedStack.pop();
	}
	
	public ArrayList<Task> getLastUnompletedState() {
		return uncompletedStack.pop();
	}
	
	public String getLastCommand() {
		return pastCommands.remove(pastCommands.size() - 1);
	}
	
	public String undo() {
		if (pastCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		localStorage.revertToPreviousState(getLastCompletedState(), getLastUnompletedState());
		return getLastCommand() + " has been undone";
	}
	
	public void storePreviousStateIfNeeded(String command) {
		if (localStorage.hasBeenChanged()) {
			addPreviousState(localStorage.getFormerCompletedTasks(), localStorage.getFormerUncompletedTasks(), command);
			localStorage.setUnchanged();
		}
	}
	
	
}
