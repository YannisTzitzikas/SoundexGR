# SoundexGR
A Soundex-like phonetic algorithm for the Greek language.

# Examples
<pre>

      word  --> SGRSimp | SGRExtra
     Θάλασσα -->   θ740  |  θ*7* 
     θάλλασα -->   θ740  |  θ*7* 
      θάλασα -->   θ740  |  θ*7* 
      μήνυμα -->   μ880  |  μ@8@ 
      μύνημα -->   μ880  |  μ@8@ 
      μίνιμα -->   μ880  |  μ@8@ 
    μοίνειμα -->   μ880  |  μ@8@ 
     έτοιμος -->   έ384  |  ε3@8 
      έτιμος -->   έ384  |  ε3@8 
      έτημος -->   έ384  |  ε3@8 
      έτυμος -->   έ384  |  ε3@8 
      έτιμως -->   έ384  |  ε3@8 
     αίτημος -->   α384  |  ε3@8 
        αυγό -->   α200  |  α12& 
        αβγό -->   α120  |  α12& 
   αυγολάκια -->   α276  |  α12& 
        αβγά -->   α120  |  α12* 
        αυγά -->   α200  |  α12* 
        αβγά -->   α120  |  α12* 
    τζατζίκι -->   τ434  |  c*4@ 
    τσατζίκι -->   τ434  |  c*4@ 
    τσατσίκι -->   τ434  |  c*4@ 
   κορονοιός -->   κ!84  |  κ&9& 
   κοροναιός -->   κ!84  |  κ&9& 
     οβελίας -->   ο174  |  ο1^7 
     ωβελύας -->   ω174  |  ο1^7 
   οβελίσκος -->   ο174  |  ο1^7 
    Βαγγέλης -->   β274  |  β*z^ 
    Βαγκέλης -->   β267  |  β*z^ 
   Βαγκαίλης -->   β267  |  β*z^ 
     Γιάννης -->   γ840  |  γ@*8 
      Γιάνης -->   γ840  |  γ@*8 
    Γιάνννης -->   γ840  |  γ@*8 
 αναδιατάσσω -->   α833  |  α8*3 
  αναδιέταξα -->   α833  |  α8*3 
       θαύμα -->   θ800  |  θ*18 
       θάβμα -->   θ180  |  θ*18 
  θαυμαστικό -->   θ843  |  θ*18 
</pre>

# Context
 This work has been done in the context of the Diploma thesis of Antrei Kavros under the supervision of Yannis Tzitzikas,
 in the Computer Science Department of the  University of Crete, GREEECE.
 
 
 # How to Run:
 * client/Examples: prints indicative examples
 * client/Prompt: for computing the codes for words inputed through the  command line
 * evaluation/BulkCheck: for running  experiments  over an evaluation collection that is included (in the folder Resources)
 
 # Persons Involved:
 * Antrei Kavros
 * Yannis Tzitzikas
