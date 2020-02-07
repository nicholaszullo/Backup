#define SEM_H
#include <linux/sched.h>
struct cs1550_sem
{
	int value;									//The value of the semaphore
	int pqnum;									//The number of processes in the PQ
	struct Node* head;							//The head of the PQ
};
struct Node
{
	struct Node* next;							//The next node in the PQ
	struct task_struct* data;					//The process stored in the node
	int priority;								//The priority of the process
};

