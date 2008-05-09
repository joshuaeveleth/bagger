package gov.loc.repository.serviceBroker.dao;

import java.util.List;

import gov.loc.repository.serviceBroker.ServiceRequest;

public interface ServiceRequestDAO {
	
	public void save(ServiceRequest req);
	
	public List<ServiceRequest> findServiceRequests(boolean includeRequestAcknowledged, boolean includeResponded, boolean includeResponseAcknowledged);
	
	public ServiceRequest findNextServiceRequest(String[] queues, String[] jobTypes, String responder);
	
	public List<ServiceRequest> findAcknowledgedServiceRequestsWithoutResponses(String responder);
	
	public ServiceRequest findNextServiceRequestWithResponse(String requester);
	
	public List<ServiceRequest> findServiceRequests(String correlationKey);
	
	public ServiceRequest findServiceRequest(Long key);
}