package gov.loc.repository.workflow.actionhandlers;

import static gov.loc.repository.workflow.WorkflowConstants.TRANSITION_CONTINUE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.taskmgmt.exe.TaskInstance;

import gov.loc.repository.packagemodeler.agents.Person;
import gov.loc.repository.packagemodeler.events.filelocation.FileCopyEvent;
import gov.loc.repository.packagemodeler.events.filelocation.FileLocationEvent;
import gov.loc.repository.packagemodeler.packge.FileLocation;
import gov.loc.repository.workflow.actionhandlers.annotations.Required;

import java.util.Calendar;
import java.text.MessageFormat;

public class AddFileCopyEventActionHandler extends BaseActionHandler {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(AddFileCopyEventActionHandler.class);

	@Required
	public String srcFileLocationKey;
	
	@Required
	public String destFileLocationKey;
	
	private FileLocation srcFileLocation;
	
	private FileLocation destFileLocation;
	
	public AddFileCopyEventActionHandler(String actionHandlerConfiguration) {
		super(actionHandlerConfiguration);
	}
	
	@Override
	protected void initialize() throws Exception
	{
		this.srcFileLocation = this.getDAO().loadRequiredFileLocation(Long.parseLong(this.srcFileLocationKey));
		this.destFileLocation = this.getDAO().loadRequiredFileLocation(Long.parseLong(this.destFileLocationKey));
	}
		
	@SuppressWarnings("unchecked")
	@Override
	protected void execute() throws Exception {
		
		FileCopyEvent event = this.getFactory().createFileLocationEvent(FileCopyEvent.class, destFileLocation, Calendar.getInstance().getTime(), this.getWorkflowAgent());				
		event.setFileLocationSource(srcFileLocation);
		
		TaskInstance taskInstance = this.executionContext.getTaskInstance();		
		//EventStart
		if (taskInstance.getStart() != null)
		{
			Calendar start = Calendar.getInstance();
			start.setTime(taskInstance.getStart());
			event.setEventStart(start.getTime());
		}
		//EventEnd
		if (taskInstance.getEnd() != null)
		{
			Calendar end = Calendar.getInstance();
			end.setTime(taskInstance.getEnd());
			event.setEventEnd(end.getTime());
		}
		//PerformingAgent
		event.setPerformingAgent(this.getDAO().findRequiredAgent(Person.class, taskInstance.getActorId()));
		
		//RequestingAgent
		event.setRequestingAgent(this.getWorkflowAgent());
		//Success
		if (! TRANSITION_CONTINUE.equals((String)this.executionContext.getContextInstance().getTransientVariable("transition")))
		{
			event.setSuccess(false);
		}
						
		log.debug(MessageFormat.format("Adding File Copy Event to {0}.  Event success: {1}", this.destFileLocation.toString(), event.isSuccess()));		
	}

}