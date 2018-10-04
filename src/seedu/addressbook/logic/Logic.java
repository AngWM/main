package seedu.addressbook.logic;

import seedu.addressbook.commands.Command;
import seedu.addressbook.commands.CommandResult;
import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.RMS;
import seedu.addressbook.data.order.ReadOnlyOrder;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.parser.Parser;
import seedu.addressbook.storage.RMSStorageFile;
import seedu.addressbook.storage.StorageFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents the main Logic of the AddressBook.
 */
public class Logic {


    private StorageFile storage;
    private RMSStorageFile rmsStorage;
    private AddressBook addressBook;
    private RMS rms;

    /** The list of person shown to the user most recently.  */
    private List<? extends ReadOnlyPerson> lastShownList = Collections.emptyList();

    /** The list of order shown to the user most recently.  */
    private List<? extends ReadOnlyOrder> lastShownOrderList = Collections.emptyList();

    public Logic() throws Exception{
        setStorage(initializeStorage());
        setAddressBook(storage.load());

        setRMSStorage(initializeRMSStorage());
        setRMS(rmsStorage.load());
    }

    Logic(StorageFile storageFile, AddressBook addressBook){
        setStorage(storageFile);
        setAddressBook(addressBook);
    }

    Logic(StorageFile storageFile, AddressBook addressBook, RMSStorageFile rmsStorageFile, RMS rms){
        setStorage(storageFile);
        setAddressBook(addressBook);

        setRMSStorage(rmsStorageFile);
        setRMS(rms);
    }

    void setStorage(StorageFile storage){
        this.storage = storage;
    }

    void setRMSStorage(RMSStorageFile rmsStorage) {
        this.rmsStorage = rmsStorage;
    }

    void setAddressBook(AddressBook addressBook){
        this.addressBook = addressBook;
    }

    void setRMS(RMS rms) {
        this.rms = rms;
    }

    /**
     * Creates the StorageFile object based on the user specified path (if any) or the default storage path.
     * @throws StorageFile.InvalidStorageFilePathException if the target file path is incorrect.
     */
    private StorageFile initializeStorage() throws StorageFile.InvalidStorageFilePathException {
        return new StorageFile();
    }

    /**
     * Creates the RMSStorageFile object based on the user specified path (if any) or the default storage path.
     * @throws RMSStorageFile.InvalidStorageFilePathException if the target file path is incorrect.
     */
    private RMSStorageFile initializeRMSStorage() throws RMSStorageFile.InvalidStorageFilePathException {
        return new RMSStorageFile();
    }

    public String getStorageFilePath() {
        return storage.getPath();
    }

    public String getOrderListFilePath() {
        return rmsStorage.getOrderListPath();
    }

    /**
     * Unmodifiable view of the current last shown list.
     */
    public List<ReadOnlyPerson> getLastShownList() {
        return Collections.unmodifiableList(lastShownList);
    }

    /**
     * Unmodifiable view of the current last shown order list.
     */
    public List<ReadOnlyOrder> getLastShownOrderList() {
        return Collections.unmodifiableList(lastShownOrderList);
    }

    protected void setLastShownList(List<? extends ReadOnlyPerson> newList) {
        lastShownList = newList;
    }

    protected void setLastShownOrderList(List<? extends ReadOnlyOrder> newList) {
        lastShownOrderList = newList;
    }

    /**
     * Parses the user command, executes it, and returns the result.
     * @throws Exception if there was any problem during command execution.
     */
    public CommandResult execute(String userCommandText) throws Exception {
        Command command = new Parser().parseCommand(userCommandText);
        CommandResult result = execute(command);
        recordResult(result);
        return result;
    }

    /**
     * Executes the command, updates storage, and returns the result.
     *
     * @param command user command
     * @return result of the command
     * @throws Exception if there was any problem during command execution.
     */
    private CommandResult execute(Command command) throws Exception {
        command.setData(addressBook, lastShownList);
        command.setRMSData(rms, lastShownOrderList);
        CommandResult result = command.execute();
        storage.save(addressBook);
        rmsStorage.save(rms);
        return result;
    }

    /** Updates the {@link #lastShownList} if the result contains a list of Persons. */
    private void recordResult(CommandResult result) {
        final Optional<List<? extends ReadOnlyPerson>> personList = result.getRelevantPersons();
        if (personList.isPresent()) {
            lastShownList = personList.get();
        }
    }
}
