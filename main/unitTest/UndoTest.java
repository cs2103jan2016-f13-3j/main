//@@author Jie Wei
package unitTest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import Logic.Undo;

public class UndoTest {

	private static Undo undoObject = Undo.getInstance();

	@Test
	public void testInitialEmptyUndoAndRedo() {
		// Wipe redo stack in case any other test methods were run before this
		undoObject.clearRedoCommands();

		// Check for zero initial undo-able commands
		assertEquals(0, undoObject.getHistoryCount());

		// Check for zero initial redo-able commands
		System.out.println(undoObject.getRedoCount());
		assertEquals(0, undoObject.getRedoCount());
	}

	@Test
	public void testAddingToUndoStack() throws ClassNotFoundException, IOException {
		// Simulate running an add task command
		undoObject.storePreviousState("Add Buy eggs ` on Friday");

		// Check for one undo-able commands that was just added
		assertEquals(1, undoObject.getHistoryCount());
	}

	@Test
	public void testUndo() throws ClassNotFoundException, IOException {
		// Check for correct retrieval of the undo command name
		String undoneCommand = undoObject.getLastCommand();
		assertEquals("Add Buy eggs ` on Friday", undoneCommand);

		// Check for no undo-able commands after pseudo-undo
		assertEquals(0, undoObject.getHistoryCount());
	}

	@Test
	public void testRedo() throws ClassNotFoundException, IOException {
		// Helper method to simulate undo (and thus adding of command to redo stack)
		undoObject.testFunction();

		// Check for correct retrieval of the undo command name
		String redoneCommand = undoObject.getRedoneCommand();
		assertEquals("Add Buy eggs ` on Friday", redoneCommand);

		// Check for one redo-able commands after pseudo-undo
		undoObject.testFunction();
		assertEquals(1, undoObject.getRedoCount());
	}
}
