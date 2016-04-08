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
	private static final String UNDO_HISTORY_HEADER = "Here are the commands you can undo, starting from the top: \n";
	private static final String REDO_HISTORY_HEADER = "Here are the commands you can redo, starting from the top: \n";
	private static final String REDO_CONFIRMATION = " has been redone";
	private static Undo undo;
	private Stack<ArrayList<Task>> completedStack, uncompletedStack, floatingStack, completedRedoStack, uncompletedRedoStack, floatingRedoStack;
	private ArrayList<String> undoCommands, redoCommands;
	private ArrayList<Task> uncompletedTasksSnapshot, completedTasksSnapshot, floatingTasksSnapshot;

	private Undo() { //constructor
		completedStack = new Stack<ArrayList<Task>>();
		uncompletedStack = new Stack<ArrayList<Task>>();
		floatingStack = new Stack<ArrayList<Task>>();
		undoCommands = new ArrayList<String>();

		completedRedoStack = new Stack<ArrayList<Task>>();
		uncompletedRedoStack = new Stack<ArrayList<Task>>();
		floatingRedoStack = new Stack<ArrayList<Task>>();
		redoCommands = new ArrayList<String>();
	}

	public static Undo getInstance() { //singleton
		if (undo == null) {
			undo = new Undo();
		}
		return undo;
	}
///////////////////////////////////////////////////////////////// REFAC the 2 view methods below
	/**
	 * Show a list of commands that can be undone.
	 * The first command displayed is the latest command that was done.
	 * 
	 * @return A list of all possible undo-able commands, if any. 
	 */
	public String viewPastCommands() {
		if (undoCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		
		int count = 0;
		String result = "";
		result += UNDO_HISTORY_HEADER;
		for (int i = undoCommands.size() - 1; i >= 0; i--) {
			count++;		
			result += count + ". " + undoCommands.get(i) + "\n";
		}
		return result.substring(0, result.length() - 1);
	}
///////////////////////////////////////////////////////////////////// REFAC
	/**
	 * Show a list of commands that can be redone.
	 * The first command displayed is the latest command that was undone.
	 * 
	 * @return A list of all possible redo-able commands, if any. 
	 */
	public String viewRedoCommands() {
		if (redoCommands.isEmpty()) {
			return NO_REDO_COMMAND;
		}
		
		int count = 0;
		String result = "";
		result += REDO_HISTORY_HEADER;
		for (int i = redoCommands.size() - 1; i >= 0; i--) {
			count++;
			result += count + ". " + redoCommands.get(i) + "\n";
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

	public ArrayList<Task> getLastRedoCompletedState() {
		return completedRedoStack.pop();
	}

	public ArrayList<Task> getLastRedoUnompletedState() {
		return uncompletedRedoStack.pop();
	}

	public ArrayList<Task> getLastRedoFloatingState() {
		return floatingRedoStack.pop();
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

	/**
	 * Undo a command by replacing the current storage arraylists with the arraylists copied from the previous program state.
	 * It will first copy and store current program state and the command input into the redo stack before proceeding. 
	 * Does not proceed if there are no commands to undo.
	 * 
	 * @return Feedback on method outcome.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String undo() throws ClassNotFoundException, IOException {
		if (undoCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		String lastCommand = getLastCommand();

		copyCurrentTasksState();
		storeCurrentStateForRedo(lastCommand); // store current snapshots for redo purposes

		localStorage.revertToPreviousState(getLastCompletedState(), getLastUnompletedState(), getLastFloatingState());
		return "\"" + lastCommand + "\"" + UNDO_CONFIRMATION;
	}

	/**
	 * Redo a command by replacing the current storage arraylists with the arraylists copied from the previous program state.
	 * It will first copy and store current program state and the command input into the undo stack before proceeding. 
	 * Does not proceed if there are no commands to redo.
	 * 
	 * @return Feedback on method outcome.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String redo() throws ClassNotFoundException, IOException {
		if (redoCommands.isEmpty()) {
			return NO_REDO_COMMAND;
		}
		String redoneCommand = getRedoneCommand();

		copyCurrentTasksState();
		storePreviousState(redoneCommand); // store current "snapshots" for undo purposes

		localStorage.revertToPreviousState(getLastRedoCompletedState(), getLastRedoUnompletedState(), getLastRedoFloatingState());
		return "\"" + redoneCommand + "\"" + REDO_CONFIRMATION;
	}

	/**
	 * Create fresh copies of all the storage arraylists.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void copyCurrentTasksState() throws ClassNotFoundException, IOException {
		uncompletedTasksSnapshot = copyArrayList(Storage.localStorage.getUncompletedTasks());
		completedTasksSnapshot = copyArrayList(Storage.localStorage.getCompletedTasks());
		floatingTasksSnapshot = copyArrayList(Storage.localStorage.getFloatingTasks());
		
	}

	/**
	 * Creates a fresh copy of an arraylist and its task contents such that both of them do not share any references.
	 * 
	 * @param sourceArray The arraylist to be copied from.
	 * @return            The copied arraylist.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static ArrayList<Task> copyArrayList(ArrayList<Task> sourceArray) throws IOException, ClassNotFoundException {	
		ArrayList<Task> CopyOfArraylist = new ArrayList<Task>(sourceArray.size());
		
		// convert each task to bytes
		for (Task t : sourceArray) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(t);
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();

			// restore task from bytes and add to the new arraylist
			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			Task tempTask = (Task) new ObjectInputStream(bais).readObject();
			CopyOfArraylist.add(tempTask);
		}
		return CopyOfArraylist;
	}

	/**
	 * Store all current copied storage arraylists into undo stack to be used for undoing the current command.
	 * Also stores the current command string to be used as feedback when undo is performed.
	 * 
	 * @param command The entire command input entered by the user.
	 */
	public void storePreviousState(String command) {
		completedStack.push(completedTasksSnapshot);
		uncompletedStack.push(uncompletedTasksSnapshot);
		floatingStack.push(floatingTasksSnapshot);
		undoCommands.add(command);
	}

	/**
	 * Store all current copied storage arraylists into redo stack to be used for redoing the current command.
	 * Also stores the current command string to be used as feedback when redo is performed.
	 * 
	 * @param command The entire command input that is currently being undone.
	 */
	public void storeCurrentStateForRedo(String command) {
		completedRedoStack.push(completedTasksSnapshot);
		uncompletedRedoStack.push(uncompletedTasksSnapshot);
		floatingRedoStack.push(floatingTasksSnapshot);
		redoCommands.add(command);
	}

	/**
	 * Empties the redo command arraylist
	 */
	public void clearRedoCommands() {
		redoCommands.clear();
	}
}
