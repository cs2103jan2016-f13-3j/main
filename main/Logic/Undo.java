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
	
	private static final String CONFIRMATION_REDO = " has been redone";
	private static final String CONFIRMATION_UNDO = " has been undone";
	private static final String HEADER_UNDO_HISTORY = "Here are the commands you can undo, starting from the top: \n";
	private static final String HEADER_REDO_HISTORY = "Here are the commands you can redo, starting from the top: \n";
	private static final String MSG_NO_UNDO_COMMAND = "There are no remaining commands that can be undone";
	private static final String MSG_NO_REDO_COMMAND = "There are no remaining commands that can be redone";
	private static Undo undo;
	
	private ArrayList<String> undoCommands, redoCommands;
	private ArrayList<Task> uncompletedTasksSnapshot, completedTasksSnapshot, floatingTasksSnapshot;
	private Stack<ArrayList<Task>> completedStack, uncompletedStack, floatingStack,
								   completedRedoStack, uncompletedRedoStack, floatingRedoStack;

	// Private constructor, following the singleton pattern.
	private Undo() {
		completedStack = new Stack<ArrayList<Task>>();
		uncompletedStack = new Stack<ArrayList<Task>>();
		floatingStack = new Stack<ArrayList<Task>>();
		undoCommands = new ArrayList<String>();

		completedRedoStack = new Stack<ArrayList<Task>>();
		uncompletedRedoStack = new Stack<ArrayList<Task>>();
		floatingRedoStack = new Stack<ArrayList<Task>>();
		redoCommands = new ArrayList<String>();
	}

	/**
	 * Method to access this class, following the singleton pattern.
	 * Invokes constructor if Undo has not been initialised.
	 * 
	 * @return The Undo object.
	 */
	public static Undo getInstance() { //singleton
		if (undo == null) {
			undo = new Undo();
		}
		return undo;
	}

	/**
	 * Show a list of commands that can be undone.
	 * The first command displayed is the latest command that was done.
	 * 
	 * @return A list of all possible undo-able commands, if any. 
	 */
	public String viewPastCommands() {
		if (undoCommands.isEmpty()) {
			return MSG_NO_UNDO_COMMAND;
		}

		String result = HEADER_UNDO_HISTORY;
		result = getListOfCommands(undoCommands, result);
		return result.substring(0, result.length() - 1);
	}

	/**
	 * Show a list of commands that can be redone.
	 * The first command displayed is the latest command that was undone.
	 * 
	 * @return A list of all possible redo-able commands, if any. 
	 */
	public String viewRedoCommands() {
		if (redoCommands.isEmpty()) {
			return MSG_NO_REDO_COMMAND;
		}

		String result = HEADER_REDO_HISTORY;
		result = getListOfCommands(redoCommands, result);
		return result.substring(0, result.length() - 1);
	}

	/**
	 * Appends a numbered list of commands from an arraylist to a header string.
	 * 
	 * @param sourceArrayList The arraylist from which to get commands from.
	 * @param inputString     A header string where the list of commands will be added to.
	 * @return                The updated input string now containing the list of commands.
	 */
	private String getListOfCommands(ArrayList<String> sourceArrayList, String inputString) {
		int count = 0;
		for (int i = sourceArrayList.size() - 1; i >= 0; i--) {
			count++;
			inputString += count + ". " + sourceArrayList.get(i) + "\n";
		}
		return inputString;
	}

	/**
	 * Get the completed tasks arraylist copied from the previous program state.
	 * 
	 * @return Arraylist containing completed tasks.
	 */
	public ArrayList<Task> getLastCompletedState() {
		return completedStack.pop();
	}

	/**
	 * Get the uncompleted tasks arraylist copied from the previous program state.
	 * 
	 * @return Arraylist containing uncompleted tasks.
	 */
	public ArrayList<Task> getLastUnompletedState() {
		return uncompletedStack.pop();
	}

	/**
	 * Get the floating tasks arraylist copied from the previous program state.
	 * 
	 * @return Arraylist containing floating tasks.
	 */
	public ArrayList<Task> getLastFloatingState() {
		return floatingStack.pop();
	}

	/**
	 * Get the completed tasks arraylist copied from program state before the latest undo was performed.
	 * 
	 * @return Arraylist containing completed tasks.
	 */
	public ArrayList<Task> getLastRedoCompletedState() {
		return completedRedoStack.pop();
	}

	/**
	 * Get the uncompleted tasks arraylist copied from program state before the latest undo was performed.
	 * 
	 * @return Arraylist containing uncompleted tasks.
	 */
	public ArrayList<Task> getLastRedoUnompletedState() {
		return uncompletedRedoStack.pop();
	}

	/**
	 * Get the floating tasks arraylist copied from program state before the latest undo was performed.
	 * 
	 * @return Arraylist containing floating tasks.
	 */
	public ArrayList<Task> getLastRedoFloatingState() {
		return floatingRedoStack.pop();
	}

	/**
	 * Get the entire user-entered command of the action that is going to be undone.
	 * 
	 * @return String of the entire command.
	 */
	public String getLastCommand() {
		return undoCommands.remove(undoCommands.size() - 1);
	}

	/**
	 * Get the entire user-entered command of the action that is going to be redone.
	 * 
	 * @return String of the entire command.
	 */
	public String getRedoneCommand() {
		return redoCommands.remove(redoCommands.size() - 1);
	}

	/**
	 * Get the number of stored commands that can be undone.
	 * 
	 * @return Number of undo-able commands.
	 */
	public int getHistoryCount() {
		return undoCommands.size();
	}


	/**
	 * Get the number of stored commands that can be redone.
	 * 
	 * @return Number of redo-able commands.
	 */
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
			return MSG_NO_UNDO_COMMAND;
		}
		
		String lastCommand = getLastCommand();

		copyCurrentTasksState();	
		storeCurrentStateForRedo(lastCommand);

		localStorage.revertToPreviousState(getLastCompletedState(), getLastUnompletedState(), getLastFloatingState());
		return "\"" + lastCommand + "\"" + CONFIRMATION_UNDO;
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
			return MSG_NO_REDO_COMMAND;
		}
		
		String redoneCommand = getRedoneCommand();

		copyCurrentTasksState();
		storePreviousState(redoneCommand); // store current "snapshots" for undo purposes

		localStorage.revertToPreviousState(getLastRedoCompletedState(), getLastRedoUnompletedState(), getLastRedoFloatingState());
		return "\"" + redoneCommand + "\"" + CONFIRMATION_REDO;
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

		for (Task t : sourceArray) {
			// convert each task to bytes
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
	private void storeCurrentStateForRedo(String command) {
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
