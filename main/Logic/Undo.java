//@@author Jie Wei
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
	private static final String NO_REDO_COMMAND = "There are no remaining commands that can be redone";
	private static final String UNDO_CONFIRMATION = " has been undone";
	private static final String REDO_CONFIRMATION = " has been redone";
	private static Undo undo;
	private Stack<ArrayList<Task>> completedStack, uncompletedStack, floatingStack, recurringStack, completedRedoStack, uncompletedRedoStack, floatingRedoStack, recurringRedoStack;
	private ArrayList<String> undoCommands, redoCommands;
	private ArrayList<Task> uncompletedTasksSnapshot, completedTasksSnapshot, floatingTasksSnapshot, recurringTasksSnapshot;

	private Undo() { //constructor
		completedStack = new Stack<ArrayList<Task>>();
		uncompletedStack = new Stack<ArrayList<Task>>();
		floatingStack = new Stack<ArrayList<Task>>();
		recurringStack = new Stack<ArrayList<Task>>();
		undoCommands = new ArrayList<String>();
		
		completedRedoStack = new Stack<ArrayList<Task>>();
		uncompletedRedoStack = new Stack<ArrayList<Task>>();
		floatingRedoStack = new Stack<ArrayList<Task>>();
		recurringRedoStack = new Stack<ArrayList<Task>>();
		redoCommands = new ArrayList<String>();
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
		if (undoCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		int count = 0;
		for (String s : undoCommands) {
			count++;
			result += count + ". " + s + "\n";
		}
		return result.substring(0, result.length() - 1);
	}
	
	// returns string containing a list of redo-able commands, if any
		public String viewRedoCommands() {
			String result = "";
			if (redoCommands.isEmpty()) {
				return NO_REDO_COMMAND;
			}
			int count = 0;
			for (String s : redoCommands) {
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
	
	public ArrayList<Task> getLastRecurringState() {
		return recurringStack.pop();
	}
	
	public ArrayList<Task> getLastRedoCompletedState() {
		return completedRedoStack.pop();
	}

	public ArrayList<Task> getLastRedoUnompletedState() {
		return uncompletedRedoStack.pop();
	}
	
	public ArrayList<Task> getLastRedoFloatingState() {
		return floatingRedoStack.pop();
	}
	
	public ArrayList<Task> getLastRedoRecurringState() {
		return recurringRedoStack.pop();
	}

	public String getLastCommand() {
		return undoCommands.remove(undoCommands.size() - 1);
	}
	
	public String getRedoneCommand() {
		return redoCommands.remove(redoCommands.size() - 1);
	}
	
	public int getHistoryCount() {
		return undoCommands.size();
	}
	
	public int getRedoCount() {
		return redoCommands.size();
	}

	// replaces current state in localStorage to a "snapshot" taken previously
	public String undo() throws ClassNotFoundException, IOException {
		if (undoCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		String lastCommand = getLastCommand();
		
		copyCurrentTasksState();
		storeCurrentStateForRedo(lastCommand); // store current snapshots for redo purposes
		
		localStorage.revertToPreviousState(getLastCompletedState(), getLastUnompletedState(), getLastFloatingState(), getLastRecurringState());
		return "\"" + lastCommand + "\"" + UNDO_CONFIRMATION;
	}
	
	// replaces current state in localStorage to a "snapshot" taken previously
		public String redo() throws ClassNotFoundException, IOException {
			if (redoCommands.isEmpty()) {
				return NO_REDO_COMMAND;
			}
			String redoneCommand = getRedoneCommand();
			
			copyCurrentTasksState();
			storePreviousState(redoneCommand); // store current snapshots for undo purposes
			
			localStorage.revertToPreviousState(getLastRedoCompletedState(), getLastRedoUnompletedState(), getLastRedoFloatingState(), getLastRedoRecurringState());
			return "\"" + redoneCommand + "\"" + REDO_CONFIRMATION;
		}

	// copy all 3 task arraylists as "snapshots" of the current program state
	public void copyCurrentTasksState() throws ClassNotFoundException, IOException {
		uncompletedTasksSnapshot = copyArrayList(Storage.localStorage.getUncompletedTasks());
		completedTasksSnapshot = copyArrayList(Storage.localStorage.getCompletedTasks());
		floatingTasksSnapshot = copyArrayList(Storage.localStorage.getFloatingTasks());
		recurringTasksSnapshot = copyArrayList(Storage.localStorage.getRecurringTasks());
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
		completedStack.push(completedTasksSnapshot);
		uncompletedStack.push(uncompletedTasksSnapshot);
		floatingStack.push(floatingTasksSnapshot);
		recurringStack.push(recurringTasksSnapshot);
		undoCommands.add(command);
	}
	
	public void storeCurrentStateForRedo(String command) {
		completedRedoStack.push(completedTasksSnapshot);
		uncompletedRedoStack.push(uncompletedTasksSnapshot);
		floatingRedoStack.push(floatingTasksSnapshot);
		recurringRedoStack.push(recurringTasksSnapshot);
		redoCommands.add(command);
	}
}
