import "@typespec/http";
import "@typespec/rest";

using Http;
@service(#{ title: "Appointment Management" })
namespace appointments;

enum UserRole {
  Owner,
  Guest,
}

model User {
  id: int64;
  name: string;
  role: UserRole;
}

model EventType {
  id: int64;
  user_id: int64;
  title: string;
  description: string;
  duration: int32;
}

enum SlotState {
  Free,
  Filled,
}

model Slot {
  id: int64;
  event_type_id: int64;
  guest_id: int64;
  title: string;
  start_time: utcDateTime;
  end_time: utcDateTime;
  state: SlotState;
}

model SlotInfo {
  id: int64;
  event_title: string;
  guest_name: string;
  slot_title: string;
  start_time: utcDateTime;
  end_time: utcDateTime;
}

@error
model Error {
  code: int32;
  message: string;
}

@route("/users")
interface Users {
  @route("/owner") @get getOwner(): {
    @statusCode statusCode: 200;
    @body body: User;
  } | {
    @statusCode statusCode: 404;
    @body body: Error;
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };
  @route("/guest") @get getGuest(): {
    @statusCode statusCode: 200;
    @body body: User;
  } | {
    @statusCode statusCode: 404;
    @body body: Error;
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };
}

@route("/event-types")
interface EventTypes {
  @doc("List event types with pagination")
  @get list(@query user_id?: int64, @query limit?: int32, @query offset?: int32): {
    @statusCode statusCode: 200;
    @body body: EventType[];
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };

  @doc("Get event type by ID")
  @get read(@path id: int64): {
    @statusCode statusCode: 200;
    @body body: EventType;
  } | {
    @statusCode statusCode: 404;
    @body body: Error;
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };

  @doc("Create event type")
  @post create(@body body: EventType): {
    @statusCode statusCode: 201;
    @body body: EventType;
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };

  @doc("Update event type")
  @put update(@path id: int64, @body body: EventType): {
    @statusCode statusCode: 200;
    @body body: EventType;
  } | {
    @statusCode statusCode: 404;
    @body body: Error;
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };

  @doc("Delete event type")
  @delete delete(@path id: int64): {
    @statusCode statusCode: 204;
  } | {
    @statusCode statusCode: 404;
    @body body: Error;
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };
}

@route("/slots")
interface Slots {
  @doc("Update slot")
  @put update(@path id: int64, @body body: Slot): {
    @statusCode statusCode: 200;
    @body body: Slot;
  } | {
    @statusCode statusCode: 404;
    @body body: Error;
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };

  @doc("List filled slots by owner")
  @route("/by-owner") @get listByOwner(@query owner_id: int64, @query start_time: plainDate, @query limit?: int32, @query offset?: int32): {
    @statusCode statusCode: 200;
    @body body: Slot[];
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };

  @doc("List slots by event type and date")
  @route("/by-event-type") @get listByEventType(@query event_type_id: int64, @query date: plainDate): {
    @statusCode statusCode: 200;
    @body body: Slot[];
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };
}

@route("/slot-infos")
interface SlotInfos {
  @doc("List slot infos by owner with pagination")
  @get listByOwner(@query owner_id: int64, @query start_time: plainDate, @query limit?: int32, @query offset?: int32): {
    @statusCode statusCode: 200;
    @body body: SlotInfo[];
  } | {
    @statusCode statusCode: 500;
    @body body: Error;
  };
}
