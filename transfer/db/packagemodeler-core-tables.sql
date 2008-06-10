create schema core;

create table core.repository (
	pkey int8 not null, 
	repository_id varchar(255) not null, 
	primary key (pkey), 
	unique (repository_id)
);

create table core.package (
	pkey int8 not null, 
	package_id varchar(255) not null, 
	processinstance_id int8, 
	repository_key int8 not null, 
	create_timestamp timestamp not null, 
	update_timestamp timestamp not null, 
	primary key (pkey), 
	unique (repository_key, package_id)
);

create table core.canonicalfile (
	pkey int8 not null, 
	bytes int8, 
	relative_path varchar(255) not null, 
	base_name varchar(255) not null, 
	extension varchar(10), 
	package_key int8 not null, 
	create_timestamp timestamp not null, 
	update_timestamp timestamp not null, 
	primary key (pkey), 
	unique (package_key, relative_path, base_name, extension)
);

create table core.canonicalfile_fixity (
	canonicalfile_key int8 not null, 
	fixity_value varchar(255) not null, 
	algorithm varchar(255) not null, 
	primary key (canonicalfile_key, fixity_value, algorithm), 
	unique (canonicalfile_key, algorithm)
);


create table core.filelocation (
	pkey int8 not null, 
	package_key int8 not null, 
	is_managed bool not null, 
	is_bag bool not null, 
	create_timestamp timestamp not null, 
	update_timestamp timestamp not null, 
	primary key (pkey)
);

create table core.fileinstance (
	pkey int8 not null, 
	file_create_timestamp timestamp, 
	relative_path varchar(255) not null, 
	base_name varchar(255) not null, 
	extension varchar(10), 
	filelocation_key int8 not null, 
	create_timestamp timestamp not null, 
	update_timestamp timestamp not null, 
	primary key (pkey), 
	unique (filelocation_key, relative_path, base_name, extension)
);

create table core.fileinstance_fixity (
	fileinstance_key int8 not null, 
	fixity_value varchar(255) not null, 
	algorithm varchar(255) not null, 
	primary key (fileinstance_key, fixity_value, algorithm), 
	unique (fileinstance_key, algorithm)
);

create table core.storagesystem_filelocation (
	pkey int8 not null, 
	base_path varchar(255) not null, 
	storagesystem_key int8 not null, 
	primary key (pkey), 
	unique (storagesystem_key, base_path)
);

create table core.external_filelocation (
	pkey int8 not null, 
	base_path varchar(255), 
	identifier_type varchar(255), 
	identifier_value varchar(255), 
	media_type varchar(255), 
	primary key (pkey) 
);

create schema agent; 

create table agent.agent (
	pkey int8 not null, 
	agent_id varchar(255) not null, 
	agent_type varchar(31) not null, 
	name varchar(255), 
	first_name varchar(255), 
	surname varchar(255), 
	primary key (pkey)
);

create table agent.agent_role (
	agent_key int8 not null, 
	role_key int8 not null, 
	primary key (agent_key, role_key)
);

create table agent.role (
	pkey int8 not null, 
	role_id varchar(255) not null, 
	primary key (pkey)
);

create table core.event_file_examination_group (
	pkey int8 not null, 
	DTYPE varchar(31) not null, 
	event_start timestamp not null, 
	event_end timestamp, 
	is_unknown_event_start bool not null, 
	is_unknown_performing_agent bool not null, 
	is_unknown_requesting_agent bool not null, 
	is_success bool not null, 
    message text,        
	file_examination_group_key int8 not null, 
	performing_agent_key int8, 
	requesting_agent_key int8, 
	reporting_agent_key int8 not null, 
	create_timestamp timestamp not null, 
	update_timestamp timestamp not null, 
	primary key (pkey)
);

create table core.event_file_location (
	pkey int8 not null, 
	DTYPE varchar(31) not null, 
	event_start timestamp not null, 
	event_end timestamp, 
	is_unknown_event_start bool not null, 
	is_unknown_performing_agent bool not null, 
	is_unknown_requesting_agent bool not null, 
	is_success bool not null,
    message text,        
	source_filelocation_key int8, 
	file_location_key int8 not null, 
	repository_system_key int8, 
	performing_agent_key int8, 
	requesting_agent_key int8, 
	reporting_agent_key int8 not null, 
	create_timestamp timestamp not null, 
	update_timestamp timestamp not null, 
	primary key (pkey)
);

create table core.event_package (
	pkey int8 not null, 
	DTYPE varchar(31) not null, 
	event_start timestamp not null, 
	event_end timestamp, 
	is_unknown_event_start bool not null, 
	is_unknown_performing_agent bool not null, 
	is_unknown_requesting_agent bool not null, 
	is_success bool not null,
    message text,    
	package_key int8 not null, 
	performing_agent_key int8, 
	requesting_agent_key int8, 
	reporting_agent_key int8 not null, 
	create_timestamp timestamp not null, 
	update_timestamp timestamp not null, 
	primary key (pkey)
);

create table core.fileexamination (
	pkey int8 not null, 
	bytes int8, 
	relative_path varchar(255) not null, 
	base_name varchar(255) not null, 
	extension varchar(10), 
	file_modified_timestamp timestamp, 
	fileexamination_group_key int8 not null, 
	create_timestamp timestamp not null, 
	update_timestamp timestamp not null, 
	primary key (pkey), 
	unique (fileexamination_group_key, relative_path, base_name, extension)
);

create table core.fileexamination_fixity (
	fileexamination_key int8 not null, 
	fixity_value varchar(255) not null, 
	algorithm varchar(255) not null, 
	primary key (fileexamination_key, fixity_value, algorithm), 
	unique (fileexamination_key, algorithm)
);

create table core.fileexamination_group (
	pkey int8 not null, 
	is_complete bool not null, 
	filelocation_key int8 not null, 
	create_timestamp timestamp not null, 
	update_timestamp timestamp not null, 
	primary key (pkey)
);

alter table agent.agent_role add constraint FKB98311709FB590E2 foreign key (role_key) references agent.role;
alter table agent.agent_role add constraint FKB983117065631594 foreign key (agent_key) references agent.agent;
alter table core.canonicalfile add constraint FK7E8C00706DD53D0D foreign key (package_key) references core.package;
alter table core.canonicalfile_fixity add constraint FK13F5B728DC43B2C1 foreign key (canonicalfile_key) references core.canonicalfile;
alter table core.event_file_examination_group add constraint FK83AF4BF33BC993B6 foreign key (performing_agent_key) references agent.agent;
alter table core.event_file_examination_group add constraint FK83AF4BF3AEED74C3 foreign key (reporting_agent_key) references agent.agent;
alter table core.event_file_examination_group add constraint FK83AF4BF38FF8DE37 foreign key (file_examination_group_key) references core.fileexamination_group;
alter table core.event_file_examination_group add constraint FK83AF4BF3B7A8A348 foreign key (requesting_agent_key) references agent.agent;
alter table core.event_file_location add constraint FKE01DF633FFB2F1C8 foreign key (file_location_key) references core.filelocation;
alter table core.event_file_location add constraint FKE01DF633D882A5E9 foreign key (repository_system_key) references agent.agent;
alter table core.event_file_location add constraint FKE01DF63383982305 foreign key (source_filelocation_key) references core.filelocation;
alter table core.event_file_location add constraint FKE01DF6333BC993B6 foreign key (performing_agent_key) references agent.agent;
alter table core.event_file_location add constraint FKE01DF633AEED74C3 foreign key (reporting_agent_key) references agent.agent;
alter table core.event_file_location add constraint FKE01DF633B7A8A348 foreign key (requesting_agent_key) references agent.agent;
alter table core.event_package add constraint FKE6D413A13BC993B6 foreign key (performing_agent_key) references agent.agent;
alter table core.event_package add constraint FKE6D413A16DD53D0D foreign key (package_key) references core.package;
alter table core.event_package add constraint FKE6D413A1AEED74C3 foreign key (reporting_agent_key) references agent.agent;
alter table core.event_package add constraint FKE6D413A1B7A8A348 foreign key (requesting_agent_key) references agent.agent;
alter table core.external_filelocation add constraint FK1D7E2125D328B0FF foreign key (pkey) references core.filelocation;
alter table core.fileexamination add constraint FK36E758F58149BCBE foreign key (fileexamination_group_key) references core.fileexamination_group;
alter table core.fileexamination_fixity add constraint FKAEA3AF038AA02E4B foreign key (fileexamination_key) references core.fileexamination;
alter table core.fileexamination_group add constraint FK9A5966B5EDDF4F61 foreign key (filelocation_key) references core.filelocation;
alter table core.fileinstance add constraint FKD1A74E11EDDF4F61 foreign key (filelocation_key) references core.filelocation;
alter table core.fileinstance_fixity add constraint FK2B9702679CE25921 foreign key (fileinstance_key) references core.fileinstance;
alter table core.filelocation add constraint FK21E059316DD53D0D foreign key (package_key) references core.package;
alter table core.package add constraint FKCFE53446E2DF0A33 foreign key (repository_key) references core.repository;
alter table core.storagesystem_filelocation add constraint FK78BB12E6D328B0FF foreign key (pkey) references core.filelocation;
alter table core.storagesystem_filelocation add constraint FK78BB12E6557FE28F foreign key (storagesystem_key) references agent.agent;
create sequence hibernate_sequence;
