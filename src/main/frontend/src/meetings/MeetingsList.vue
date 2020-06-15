<template>
  <table v-if="meetings.length > 0">
    <thead>
    <tr>
      <th>Nazwa spotkania</th>
      <th>Opis</th>
      <th>Uczestnicy</th>
      <td></td>
    </tr>
    </thead>
    <tbody>
    <tr v-for="meeting in meetings" :key="meeting.id">
      <td>{{ meeting.title }}</td>
      <td>{{ meeting.description }}</td>
      <td>
        <ul v-if="meeting.participants">
          <li v-for="participant in meeting.participants" :key="participant">
            {{ participant.login }}
          </li>
        </ul>
      </td>
      
      <td style="text-align: right; min-width: 400px">
        <button v-if="!checkIfEnrolled(meeting)" class="button-outline" @click="$emit('attend', meeting)">
<!--         <button v-if="meeting.participants.findIndex(p => p.login === username) < 0" class="button-outline" --> 
          Zapisz się
        </button>
<!--         <button v-if="checkIfEnrolled(meeting)" class="button-outline" @click="$emit('unattend', meeting)">Wypisz się</button> -->
		<button v-else class="button-outline" @click="$emit('unattend', meeting)">Wypisz się</button>
        <button v-if="meeting.participants == null || meeting.participants.length === 0" class="button" @click="$emit('delete', meeting)">
          Usuń puste spotkanie
        </button> 
      </td> 
    </tr>
    </tbody>
  </table>
</template>

<script>
    export default {
        props: ['meetings', 'username'],
        methods: {
          checkIfEnrolled(meeting) {
         	for (var participant in meeting.participants){
        		if (participant.name === username) return true;
        	}
        	return false; 
        }
    }
    }
</script>
