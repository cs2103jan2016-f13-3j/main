package Logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Stack;

import Storage.localStorage;
import Task.Task;

public class Undo {
	private static final String NO_PAST_COMMAND = "There are no remaining commands that can be undone";
	private static Undo undo;
	private Stack<ArrayList<Task>> completedStack, uncompletedStack, floatingStack;
	private ArrayList<String> pastCommands;
	private ArrayList<Task> UncompletedTasksSnapshot, CompletedTasksSnapshot, FloatingTasksSnapshot;

	private Undo() { //constructor
		completedStack = new Stack<ArrayList<Task>>();
		uncompletedStack = new Stack<ArrayList<Task>>();
		floatingStack = new Stack<ArrayList<Task>>();
		pastCommands = new ArrayList<String>();
	}

	public static Undo getInstance() { //singleton
		if (undo == null) {
			undo = new Undo();
		}
		return undo;
	}

	// returns string containing a list of undo-able commands, if any
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

	public ArrayList<Task> getLastCompletedState() {
		return completedStack.pop();
	}

	public ArrayList<Task> getLastUnompletedState() {
		return uncompletedStack.pop();
	}
	
	public ArrayList<Task> getLastFloatingState() {
		return floatingStack.pop();
	}

	public String getLastCommand() {
		return pastCommands.remove(pastCommands.size() - 1);
	}

	// replaces current state in localStorage to a "snapshot" taken previously
	public String undo() {
		if (pastCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		localStorage.revertToPreviousState(getLastCompletedState(), getLastUnompletedState(), getLastFloatingState());
		return getLastCommand() + " has been undone";
	}

	// copy all 3 task arraylists as "snapshots" of the current program state
	public void copyCurrentTasksState() throws ClassNotFoundException, IOException {
		UncompletedTasksSnapshot = copyArrayList(Storage.localStorage.getUncompletedTasks());
		CompletedTasksSnapshot = copyArrayList(Storage.localStorage.getCompletedTasks());
		FloatingTasksSnapshot = copyArrayList(Storage.localStorage.getFloatingTasks());
	}
	
	// make a copy of an arraylist
	// input & output streams are used to ensure no shared references
	private static ArrayList<Task> copyArrayList(ArrayList<Task> sourceArray) throws IOException, ClassNotFoundException {	
		ArrayList<Task> CopyOfArraylist = new ArrayList<Task>(sourceArray.size());
		for (Task t : sourceArray) {
			
			// convert tasks to bytes
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(t);
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();

			// restore task from bytes and add to the copied arraylist
			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			Task tempTask = (Task) new ObjectInputStream(bais).readObject();
			CopyOfArraylist.add(tempTask);
		}
		return CopyOfArraylist;
	}

	// adds the "snapshots" of the arraylists into stacks for undo purposes. The command executed is also stored.
	public void storePreviousState(String command) {
		completedStack.push(CompletedTasksSnapshot);
		uncompletedStack.push(UncompletedTasksSnapshot);
		floatingStack.push(FloatingTasksSnapshot);
		pastCommands.add(command);
	}
	
	
}
