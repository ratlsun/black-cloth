package hale.bc.server.to;

public enum UserChangeType {
	MockActivity, 
	CreateMocker, ChangeMockerOwner, ChangeMockerType, ChangeMockerName, DeleteMocker,
	WatchMocker, UnwatchMocker,
	ChangeMockerRules
}
